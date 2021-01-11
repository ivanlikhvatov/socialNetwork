package com.example.socialNetwork.config;

import com.example.socialNetwork.component.LoggedUser;
import com.example.socialNetwork.component.MySimpleUrlAuthenticationSuccessHandler;
import com.example.socialNetwork.domain.User;
import com.example.socialNetwork.dto.AuthorityType;
import com.example.socialNetwork.dto.Role;
import com.example.socialNetwork.domain.SocialUser;
import com.example.socialNetwork.exceptions.NotFoundException;
import com.example.socialNetwork.repo.UserRepo;
import com.example.socialNetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@Configuration
@EnableWebSecurity
//securedEnabled = true
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserRepo userRepo;

    @Autowired
    UserService userService;

    @Autowired
    MySimpleUrlAuthenticationSuccessHandler mySimpleUrlAuthenticationSuccessHandler;

    @Autowired
    LoggedUser loggedUser;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/**")
                .authorizeRequests(a -> a
                        .antMatchers("/", "/login**", "/registration", "/js/**", "/error**", "/activate/*", "/auth", "/loginError", "/img/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .logout(l -> l
                        .logoutSuccessUrl("/").permitAll().addLogoutHandler((request, response, authentication) -> {
                            System.out.println("logged out 1!");
                            System.out.println(authentication.getPrincipal());
                        })
                )
                .csrf().disable()
                .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo
                        .oidcUserService(this.oidcUserService())
                ))

                .formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .failureUrl("/loginError")
                .permitAll();
    }

    public OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        final OidcUserService delegate = new OidcUserService();

        return (userRequest) -> {
            OidcUser oidcUser = delegate.loadUser(userRequest);
            Map<String, Object> attributes = oidcUser.getAttributes();
            String type = attributes.get("iss").toString();

            if ("https://accounts.google.com".equals(type)){
                String id = (String) attributes.get("sub");

                SocialUser user = (SocialUser) userRepo.findById(id).orElseGet(() -> {
                    SocialUser newUser = new SocialUser();
                    newUser.setId(id);
                    newUser.setName((String) attributes.get("name"));
                    newUser.setEmail((String) attributes.get("email"));
                    newUser.setGender((String) attributes.get("gender"));
                    newUser.setLocale((String) attributes.get("locale"));
                    newUser.setUserpic((String) attributes.get("picture"));
                    newUser.setRoles(Collections.singleton(Role.USER));
                    newUser.setAuthorityType(AuthorityType.SOCIAL);
                    newUser.setActive(true);
                    newUser.setNonLocked(true);
                    newUser.setOnline(true);

                    return newUser;
                });

                if (!user.isAccountNonLocked()){
                    throw new NotFoundException();
                }

                user.setLastVisit(LocalDateTime.now());
                userRepo.save(user);

                return user;
            }

            return null;

        };
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }
}
