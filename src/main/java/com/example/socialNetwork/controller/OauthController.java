package com.example.socialNetwork.controller;

import com.example.socialNetwork.domain.User;
import com.example.socialNetwork.repo.UserDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Map;

@Controller
@RequestMapping("/oathSign")
public class OauthController {
    @Autowired
    UserDetailsRepo userDetailsRepo;

    @GetMapping
    public String createWebUser(@AuthenticationPrincipal OidcUser oidcUser,
                                final RedirectAttributes redirectAttributes
    ){
        Map<String, Object> attributes = oidcUser.getAttributes();
        String id = (String) attributes.get("sub");

        User user = userDetailsRepo.findById(id).orElseGet(() -> {
            User newUser = new User();
            newUser.setId(id);
            newUser.setName((String) attributes.get("name"));
            newUser.setEmail((String) attributes.get("email"));
            newUser.setGender((String) attributes.get("gender"));
            newUser.setLocale((String) attributes.get("locale"));
            newUser.setUserpic((String) attributes.get("picture"));

            return newUser;
        });

        user.setLastVisit(LocalDateTime.now());
        redirectAttributes.addFlashAttribute("afterLoginUser", user);

        return "redirect:/";
    }
}
