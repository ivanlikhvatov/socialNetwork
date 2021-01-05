package com.example.socialNetwork.controller;

import com.example.socialNetwork.domain.*;
import com.example.socialNetwork.dto.MessagePageDto;
import com.example.socialNetwork.repo.UserRepo;
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
    private final UserRepo userRepo;

    @Value("${spring.profiles.active}")
    private String profile;
    private final ObjectWriter messageWriter;
    private final ObjectWriter userWriter;

    @Autowired
    public MainController(MessageService messageService, UserRepo userRepo, ObjectMapper mapper) {
        this.userRepo = userRepo;
        this.messageService = messageService;
        this.messageWriter = mapper
                .setConfig(mapper.getSerializationConfig())
                .writerWithView(Views.FullMessage.class);
        this.userWriter = mapper
                .setConfig(mapper.getSerializationConfig())
                .writerWithView(Views.FullProfile.class);
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
            MessagePageDto messagePageDto = messageService.findAll(pageRequest);

            String messages = messageWriter.writeValueAsString(messagePageDto.getMessages());

            model.addAttribute("messages", messages);
            data.put("currentPage", messagePageDto.getCurrentPage());
            data.put("totalPages", messagePageDto.getTotalPages());
        } else {
            model.addAttribute("messages", "[]");
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
