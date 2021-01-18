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
    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @JsonView(Views.FullProfile.class)
    @GetMapping
    public List<User> userList(){
        return userService.findAll();
    }

    @JsonView(Views.FullProfile.class)
    @GetMapping("/active")
    public List<User> activeUserList(){
        return userService.findActive();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @JsonView(Views.FullProfile.class)
    @GetMapping("{user}")
    public User userEditForm(@PathVariable User user){
        return user;
    }
}
