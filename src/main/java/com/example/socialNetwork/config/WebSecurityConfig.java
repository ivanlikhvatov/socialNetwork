package com.example.socialNetwork.config;

import com.example.socialNetwork.dto.AuthorityType;
import com.example.socialNetwork.dto.Role;
import com.example.socialNetwork.domain.SocialUser;
import com.example.socialNetwork.repo.UserRepo;
import com.example.socialNetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
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
                        .logoutSuccessUrl("/").permitAll()
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

                    return newUser;
                });

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






























//    @Bean
//    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService(WebClient rest) {
//        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
//        return request -> {
//            OAuth2User user = delegate.loadUser(request);
//            if (!"github".equals(request.getClientRegistration().getRegistrationId())) {
//                return user;
//            }
//
//            OAuth2AuthorizedClient client = new OAuth2AuthorizedClient
//                    (request.getClientRegistration(), user.getName(), request.getAccessToken());
//            String url = user.getAttribute("organizations_url");
//            List<Map<String, Object>> orgs = rest
//                    .get().uri(url)
//                    .attributes(oauth2AuthorizedClient(client))
//                    .retrieve()
//                    .bodyToMono(List.class)
//                    .block();
//
//            if (orgs.stream().anyMatch(org -> "spring-projects".equals(org.get("login")))) {
//                return user;
//            }
//
//            throw new OAuth2AuthenticationException(new OAuth2Error("invalid_token", "Not in Spring Team", ""));
//        };
//    }
//
//    @Bean
//    public WebClient rest(ClientRegistrationRepository clients, OAuth2AuthorizedClientRepository authz) {
//        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 =
//                new ServletOAuth2AuthorizedClientExchangeFilterFunction(clients, authz);
//        return WebClient.builder()
//                .filter(oauth2).build();
//    }


//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .antMatcher("/**")
//                .authorizeRequests()
//                .antMatchers("/", "/login**", "/js/**", "/error**").permitAll()
//                .anyRequest().authenticated()
//                .and().logout().logoutSuccessUrl("/").permitAll()
//                .and()
//                .csrf().disable();
//    }

//    @Bean
//    public PrincipalExtractor principalExtractor(UserDetailsRepo userDetailsRepo){
//
//        return map -> {
//            System.out.println("jlkjlkjlkj");
//
//            String id = (String) map.get("sub");
//            GoogleAuthUser user = userDetailsRepo.findById(id).orElseGet(() -> {
//                GoogleAuthUser newUser = new GoogleAuthUser();
//                newUser.setId(id);
//                newUser.setName((String) map.get("name"));
//                newUser.setEmail((String) map.get("email"));
//                newUser.setGender((String) map.get("gender"));
//                newUser.setLocale((String) map.get("locale"));
//                newUser.setUserpic((String) map.get("picture"));
//
//                return newUser;
//            });
//
//            user.setLastVisit(LocalDateTime.now());
//            return userDetailsRepo.save(user);
//        };
//    }
}
