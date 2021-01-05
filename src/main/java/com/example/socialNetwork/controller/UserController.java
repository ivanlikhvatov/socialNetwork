package com.example.socialNetwork.controller;

import com.example.socialNetwork.domain.Role;
import com.example.socialNetwork.domain.User;
import com.example.socialNetwork.domain.Views;
import com.example.socialNetwork.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

//    @PreAuthorize("hasAuthority('ADMIN')")
    @JsonView(Views.FullProfile.class)
    @GetMapping
    public List<User> userList(){
        return userService.findAll();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model){
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());

        return "index";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public String userSave(
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user
    ){
        userService.saveUser(user, form);

        return "index";
    }

//    @GetMapping("/profile")
//    public String getProfile(Model model, @AuthenticationPrincipal User user){
//        model.addAttribute("basketRepo", basketRepo);
//        model.addAttribute("username", user.getUsername());
//        model.addAttribute("email", user.getEmail());
//
//        return "profile";
//    }
//
//    @PostMapping("/profile")
//    public String updateProfile(
//            @RequestParam String username,
//            @AuthenticationPrincipal User user,
//            @RequestParam String password,
//            @RequestParam String email,
//            Model model
//    ){
//        if (!userService.updateProfile(user, password, email, username)){
//            model.addAttribute("message", "Не удалось изменить email, пользователь с таким email уже существует");
//            return "profile";
//        }
//
//        return "redirect:/user/profile";
//    }
}
