package com.example.socialNetwork.controller;

import com.example.socialNetwork.domain.*;
import com.example.socialNetwork.dto.MessageType;
import com.example.socialNetwork.service.GroupService;
import com.example.socialNetwork.service.MessageService;
import com.example.socialNetwork.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("group")
public class GroupController {
    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
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
        group.setCreationDate(LocalDateTime.now());
        groupService.createGroup(group);
        return group;
    }

    @GetMapping("{id}")
    @JsonView(Views.FullGroup.class)
    public Group getOne(@PathVariable("id") Group group){
        return group;
    }
}
