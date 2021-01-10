package com.example.socialNetwork.controller;

import com.example.socialNetwork.domain.User;
import com.example.socialNetwork.domain.Views;
import com.example.socialNetwork.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserRestController {
    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @JsonView(Views.FullProfile.class)
    @GetMapping
    public List<User> userList(){
        return userService.findAll();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @JsonView(Views.FullProfile.class)
    @GetMapping("{user}")
    public User userEditForm(@PathVariable User user){
        return user;
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
