package com.example.socialNetwork.controller;

import com.example.socialNetwork.domain.AuthorityType;
import com.example.socialNetwork.domain.CustomUser;
import com.example.socialNetwork.domain.User;
import com.example.socialNetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/")
public class RegistrationController {
    @Value("${spring.profiles.active}")
    private String profile;

    @Autowired
    UserService userService;

    @GetMapping("/registration")
    public String registration(Model model){
        Map<Object, Object> data = new HashMap<>();

        model.addAttribute("frontendData", data);
        model.addAttribute("isDevMode", "dev".equals(profile));
        List<String> emails = new ArrayList<>();

        for (User u : userService.findAllByAuthorityType(AuthorityType.SOCIAL)) {
            emails.add(u.getEmail());
        }
        model.addAttribute("emails", emails);

        model.addAttribute("message", "Вы успешно зарегистрировались, подтвердите свой аккаунт перейдя по ссылке");
        model.addAttribute("messages", "[]");
        model.addAttribute("profile", "null");
        return "index";
    }

    @PostMapping("/registration")
    public String addUser(CustomUser user, Model model) {


        Map<Object, Object> data = new HashMap<>();
        model.addAttribute("frontendData", data);
        model.addAttribute("isDevMode", "dev".equals(profile));

        if ((user.getEmail().length() > 255) || (StringUtils.isEmpty(user.getEmail()))
                || (user.getPassword().length() > 255) || (StringUtils.isEmpty(user.getPassword()))
                || (user.getName().length() > 255) || (StringUtils.isEmpty(user.getName()))){
            model.addAttribute("message", "Неккоректные данные");
            model.addAttribute("messages", "[]");
            model.addAttribute("profile", "null");
            return "index";
        }

        Pattern emailPattern = Pattern.compile("(([A-Za-z0-9]+[A-Za-z0-9_.\\-]+[A-Za-z0-9]+)|([A-Za-z0-9]+))@([A-Za-z0-9]{2,}[.])+(ru|com|org|net)");

        if (!Pattern.matches(emailPattern.toString(), user.getEmail())){
            model.addAttribute("message", "Неккоректные данные");
            return "index";
        }

        if (!userService.addUser(user)){
            model.addAttribute("message", "Пользователь с таким E-mail уже существует");
            return "index";
        }

        List<String> emails = new ArrayList<>();

        for (User u : userService.findAllByAuthorityType(AuthorityType.CUSTOM)) {
            emails.add(u.getEmail());
            System.out.println("reg: " + u);
        }

        model.addAttribute("emails", emails);

        model.addAttribute("message", "Вы успешно зарегистрировались, подтвердите свой аккаунт перейдя по ссылке");
        model.addAttribute("messages", "[]");
        model.addAttribute("profile", "null");

        return "index";
    }

//    @GetMapping("loginError")
//    public String loginError(Model model){
//        model.addAttribute("message", "Неверный E-mail или пароль, попробуйте ещё раз");
//        return "index";
//    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code){
        boolean isActivate = userService.activateUser(code);

        if (isActivate){
            model.addAttribute("message", "Вы успешно зарегистрировались");
        } else {
            model.addAttribute("message", "Код активации не найден!");
        }

        return "index";
    }
}
