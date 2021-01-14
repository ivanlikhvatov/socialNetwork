package com.example.socialNetwork.controller;

import com.example.socialNetwork.domain.*;
import com.example.socialNetwork.dto.GeneralMessagePageDto;
import com.example.socialNetwork.dto.MessageType;
import com.example.socialNetwork.service.MessageService;
import com.example.socialNetwork.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("message")
public class MessageController {
    public static final int MESSAGES_PER_PAGE = 11  ;

    private final MessageService messageService;
    private final UserService userService;

    @Autowired
    public MessageController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @GetMapping("/generalMessage")
    @JsonView(Views.FullMessage.class)
    public GeneralMessagePageDto generalList(
            @PageableDefault(size = MESSAGES_PER_PAGE, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
    ){
        return messageService.findGeneralMessages(pageable);
    }

    @GetMapping("/privateMessage")
    @JsonView(Views.FullMessage.class)
    public List<PrivateMessage> privateList(
            @AuthenticationPrincipal User user
    ){
        return messageService.findPrivateMessages(user);
    }

    @GetMapping("/groupMessage")
    @JsonView(Views.FullMessage.class)
    public List<GroupMessage> groupList(
            @AuthenticationPrincipal User user
    ){
        return messageService.findGroupMessages(user);
    }

    @GetMapping("{id}")
    @JsonView(Views.FullMessage.class)
    public Message getOne(@PathVariable("id") Message message){
        return message;
    }

    @PostMapping("GENERAL")
    @JsonView(Views.FullMessage.class)
    public Message create(@RequestBody GeneralMessage message,
                          @AuthenticationPrincipal User user
    ) throws IOException {
        message.setAuthor(user);
        message.setMessageType(MessageType.GENERAL);

        return messageService.create(message);
    }

    @PostMapping("PRIVATE")
    @JsonView(Views.FullMessage.class)
    public Message create(@RequestBody PrivateMessage message,
                          @AuthenticationPrincipal User user
    ) throws IOException {
        message.setAuthor(user);
        message.setMessageType(MessageType.PRIVATE);

        return messageService.create(message);
    }

    @PostMapping("GROUP")
    @JsonView(Views.FullMessage.class)
    public Message create(@RequestBody GroupMessage message,
                          @AuthenticationPrincipal User user
    ) throws IOException {
        message.setAuthor(user);
        message.setMessageType(MessageType.GROUP);

        return messageService.create(message);

    }

    @PutMapping("{id}")
    @JsonView(Views.FullMessage.class)
    public Message update(
            @PathVariable("id") Message messageFromDb,
            @RequestBody Message message,
            @AuthenticationPrincipal User user
    ) throws IOException {
        User userDb = userService.findById(user.getId());
        message.setMessageType(MessageType.GENERAL);//TODO переделать

        if (!userDb.isAccountNonLocked()){
            Message errMessage = new Message();
            User errUser = userService.findById("bot");

            errMessage.setCreationDate(LocalDateTime.now());
            errMessage.setText(user.getName() + ", Ваш аккаунт был заблокирован из-за нарушения правил нашего чата, вы можете написать администратору, либо связаться с нами по почте");
            errMessage.setAuthor(errUser);

            return messageService.create(errMessage);
        }

        return messageService.update(messageFromDb, message);
    }

    @DeleteMapping("{id}")
    public void  delete(@PathVariable("id") Message message, @AuthenticationPrincipal User user) throws IOException {
        User userDb = userService.findById(user.getId());

        if (!userDb.isAccountNonLocked()){
            Message errMessage = new Message();
            User errUser = userService.findById("bot");

            errMessage.setCreationDate(LocalDateTime.now());
            errMessage.setText(user.getName() + ", Ваш аккаунт был заблокирован из-за нарушения правил нашего чата, вы можете написать администратору, либо связаться с нами по почте");
            errMessage.setAuthor(errUser);

            messageService.create(errMessage);
            return;
        }

        messageService.delete(message);
    }

}
