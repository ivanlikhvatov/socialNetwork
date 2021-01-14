package com.example.socialNetwork.controller;

import com.example.socialNetwork.domain.*;
import com.example.socialNetwork.dto.AuthorityType;
import com.example.socialNetwork.dto.GeneralMessagePageDto;
import com.example.socialNetwork.repo.UserRepo;
import com.example.socialNetwork.service.GroupService;
import com.example.socialNetwork.service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.*;

@Controller
@RequestMapping("/")
public class MainController {
    private final MessageService messageService;
    private final GroupService groupService;
    private final UserRepo userRepo;

    @Value("${spring.profiles.active}")
    private String profile;
    private final ObjectWriter messageWriter;
    private final ObjectWriter userWriter;
    private final ObjectWriter groupWriter;

    @Autowired
    public MainController(MessageService messageService, GroupService groupService, UserRepo userRepo, ObjectMapper mapper) {
        this.groupService = groupService;
        this.userRepo = userRepo;
        this.messageService = messageService;
        this.messageWriter = mapper
                .setConfig(mapper.getSerializationConfig())
                .writerWithView(Views.FullMessage.class);
        this.userWriter = mapper
                .setConfig(mapper.getSerializationConfig())
                .writerWithView(Views.FullProfile.class);
        this.groupWriter = mapper
                .setConfig(mapper.getSerializationConfig())
                .writerWithView(Views.FullGroup.class);
    }


    @GetMapping
    public String main(Model model, @AuthenticationPrincipal User user) throws JsonProcessingException {
        Map<Object, Object> data = new HashMap<>();

        if (user != null){
            if (user.getAuthorityType().equals(AuthorityType.SOCIAL)){
                SocialUser userFromDb = (SocialUser) userRepo.findById(user.getId()).get();
                String serializedProfile = userWriter.writeValueAsString(userFromDb);
                model.addAttribute("profile", serializedProfile);
            }

            if (user.getAuthorityType().equals(AuthorityType.CUSTOM)){
                CustomUser userFromDb = (CustomUser) userRepo.findById(user.getId()).get();
                String serializedProfile = userWriter.writeValueAsString(userFromDb);
                model.addAttribute("profile", serializedProfile);
            }

            Sort sort = Sort.by(Sort.Direction.DESC, "id");
            PageRequest pageRequest = PageRequest.of(0, MessageController.MESSAGES_PER_PAGE, sort);
            GeneralMessagePageDto generalMessagePageDto = messageService.findGeneralMessages(pageRequest);
            String messages = messageWriter.writeValueAsString(generalMessagePageDto.getMessages());

            List<PrivateMessage> privateMessageList = messageService.findPrivateMessages(user);
            String privateMessages = messageWriter.writeValueAsString(privateMessageList);

            List<GroupMessage> groupMessagesList = messageService.findGroupMessages(user);
            String groupMessages = messageWriter.writeValueAsString(groupMessagesList);

            List<Group> groupList = groupService.findAllByUser(user.getId());
            String groups = groupWriter.writeValueAsString(groupList);

            model.addAttribute("groups", groups);
            model.addAttribute("messages", messages);
            model.addAttribute("groupMessages", groupMessages);
            model.addAttribute("privateMessages", privateMessages);
            data.put("currentPage", generalMessagePageDto.getCurrentPage());
            data.put("totalPages", generalMessagePageDto.getTotalPages());
        } else {
            model.addAttribute("groups", "[]");
            model.addAttribute("messages", "[]");
            model.addAttribute("groupMessages", "[]");
            model.addAttribute("privateMessages", "[]");
            model.addAttribute("profile", "null");
        }

        List<String> emails = new ArrayList<>();

        for (User u : userRepo.findAllByAuthorityType(AuthorityType.CUSTOM)) {
            emails.add(u.getEmail());
        }

        model.addAttribute("emails", emails);
        model.addAttribute("frontendData", data);
        model.addAttribute("isDevMode", "dev".equals(profile));

        return "index";
    }

}
