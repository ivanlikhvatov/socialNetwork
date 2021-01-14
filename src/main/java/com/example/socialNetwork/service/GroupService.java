package com.example.socialNetwork.service;

import com.example.socialNetwork.domain.Group;
import com.example.socialNetwork.domain.GroupMessage;
import com.example.socialNetwork.domain.Message;
import com.example.socialNetwork.domain.Views;
import com.example.socialNetwork.dto.EventType;
import com.example.socialNetwork.dto.ObjectType;
import com.example.socialNetwork.repo.GroupRepo;
import com.example.socialNetwork.util.WsSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

@Service
public class GroupService {
    private final GroupRepo groupRepo;
    private final BiConsumer<EventType, Group> wsSender;

    @Autowired
    public GroupService(GroupRepo groupRepo, WsSender wsSender) {
        this.groupRepo = groupRepo;
        this.wsSender = wsSender.getSender(ObjectType.GROUP, Views.FullGroup.class);
    }

    public void createGroup(Group group){
        groupRepo.save(group);
        wsSender.accept(EventType.CREATE, group);
    }

    public List<Group> findAllByUser(String userId) {
        List<Group> groups = groupRepo.findAll();
        List<Group> result = new ArrayList<>();

        for (Group group : groups) {
            if (group.getMembers().contains(userId)){
                result.add(group);
            }
        }

        return result;
    }

//    public Group addMessage(GroupMessage groupMessage) {
//        Group group = groupMessage.getGroup();
//
//        List<Long> temp = group.getMessages();
//        temp.add(groupMessage.getId());
//
//        group.setMessages(temp);
//        groupRepo.save(group);
//
//        wsSender.accept(EventType.UPDATE, group);
//
//        return group;
//    }
}
