package com.example.socialNetwork.controller;

import com.example.socialNetwork.domain.Group;
import com.example.socialNetwork.domain.Message;
import com.example.socialNetwork.domain.User;
import com.example.socialNetwork.domain.Views;
import com.example.socialNetwork.service.GroupService;
import com.example.socialNetwork.service.MessageService;
import com.example.socialNetwork.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("group")
public class GroupController {
    private final GroupService groupService;
    private final MessageService messageService;
    private final UserService userService;

    @Autowired
    public GroupController(GroupService groupService, MessageService messageService, UserService userService) {
        this.groupService = groupService;
        this.messageService = messageService;
        this.userService = userService;
    }

    @GetMapping
    @JsonView(Views.FullGroup.class)
    public List<Group> groupList(@AuthenticationPrincipal User user){
        return groupService.findAllByUser(user.getId());
    }

    @PostMapping
    @JsonView(Views.FullGroup.class)
    public Group createGroup(@AuthenticationPrincipal User groupAdmin, @RequestBody Group group){
        group.setAdmin(groupAdmin);
        List <String> temp = group.getMembers();
        temp.add(groupAdmin.getId());
        group.setMembers(temp);
        groupService.createGroup(group);
        return group;
    }

    @GetMapping("{id}")
    @JsonView(Views.FullGroup.class)
    public Group getOne(@PathVariable("id") Group group){
        return group;
    }
}
