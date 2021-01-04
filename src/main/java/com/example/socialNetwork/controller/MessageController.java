package com.example.socialNetwork.controller;

import com.example.socialNetwork.domain.Message;
import com.example.socialNetwork.domain.SocialUser;
import com.example.socialNetwork.domain.User;
import com.example.socialNetwork.domain.Views;
import com.example.socialNetwork.dto.MessagePageDto;
import com.example.socialNetwork.service.MessageService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

@RestController
@RequestMapping("message")
public class MessageController {
    public static final int MESSAGES_PER_PAGE = 11  ;

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    @JsonView(Views.FullMessage.class)
    public MessagePageDto list(
            @PageableDefault(size = MESSAGES_PER_PAGE, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
    ){
        return messageService.findAll(pageable);
    }

    @GetMapping("{id}")
    @JsonView(Views.FullMessage.class)
    public Message getOne(@PathVariable("id") Message message){
        return message;
    }

    @PostMapping
    @JsonView(Views.FullMessage.class)
    public Message create(@RequestBody Message message,
                          @AuthenticationPrincipal User user
    ) throws IOException {


        return messageService.create(message, user);
    }

    @PutMapping("{id}")
    @JsonView(Views.FullMessage.class)
    public Message update(
            @PathVariable("id") Message messageFromDb,
            @RequestBody Message message
    ) throws IOException {
        return messageService.update(messageFromDb, message);
    }

    @DeleteMapping("{id}")
    public void  delete(@PathVariable("id") Message message){
        messageService.delete(message);
    }

}
