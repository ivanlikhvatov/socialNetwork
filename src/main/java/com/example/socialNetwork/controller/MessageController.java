package com.example.socialNetwork.controller;

import com.example.socialNetwork.domain.*;
import com.example.socialNetwork.dto.GeneralMessagePageDto;
import com.example.socialNetwork.dto.GroupMessagePageDto;
import com.example.socialNetwork.dto.MessageType;
import com.example.socialNetwork.dto.PrivateMessagePageDto;
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

    @GetMapping("/general")
    @JsonView(Views.FullMessage.class)
    public GeneralMessagePageDto generalList(
            @PageableDefault(size = MESSAGES_PER_PAGE, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
    ){
        System.out.println("message");
        return messageService.findGeneralMessages(pageable);
    }

    @GetMapping("/private")
    @JsonView(Views.FullMessage.class)
    public PrivateMessagePageDto privateList(
            @PageableDefault(size = MESSAGES_PER_PAGE, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal User author,
            User addressee
    ){
        return messageService.findPrivateMessages(addressee, author, pageable);
    }

    @GetMapping("/group")
    @JsonView(Views.FullMessage.class)
    public GroupMessagePageDto groupList(
            @PageableDefault(size = MESSAGES_PER_PAGE, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal User author,
            List<User> addresses
    ){
        return messageService.findGroupMessages(addresses, author, pageable);
    }

    @GetMapping("{id}")
    @JsonView(Views.FullMessage.class)
    public Message getOne(@PathVariable("id") Message message){
        return message;
    }

    @PostMapping("{type}")
    @JsonView(Views.FullMessage.class)
    public Message create(@RequestBody Message message,
                          @AuthenticationPrincipal User user,
                          @PathVariable("type") MessageType type
    ) throws IOException {
        User userDb = userService.findById(user.getId());

        if (!userDb.isAccountNonLocked()){
            GeneralMessage errMessage = new GeneralMessage();
            User errUser = userService.findById("bot");

            errMessage.setCreationDate(LocalDateTime.now());
            errMessage.setText(user.getName() + ", Ваш аккаунт был заблокирован из-за нарушения правил нашего чата, вы можете написать администратору, либо связаться с нами по почте");
            errMessage.setMessageType(type);
            errMessage.setAuthor(errUser);

            return messageService.create(errMessage);
        }

        if (MessageType.GENERAL.equals(type)){
            GeneralMessage gm = new GeneralMessage();
            gm.setText(message.getText());
            gm.setAuthor(user);
            gm.setMessageType(type);

            return messageService.create(gm);
        }

        if (MessageType.PRIVATE.equals(type)){
            PrivateMessage pm = new PrivateMessage();
            pm.setText(message.getText());
            pm.setAuthor(user);
            pm.setMessageType(type);

            return messageService.create(pm);
        }

        if (MessageType.GROUP.equals(type)){
            GroupMessage gm = new GroupMessage();
            gm.setText(message.getText());
            gm.setAuthor(user);
            gm.setMessageType(type);

            return messageService.create(gm);
        }

        return null;
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
