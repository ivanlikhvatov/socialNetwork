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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;
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
        addModelData(model);
        return "index";
    }

    @PostMapping("/registration")
    public String addUser(
            CustomUser user,
            Model model,
            @RequestParam("file") MultipartFile file
    ) {
        Map<Object, Object> data = new HashMap<>();
        List<String> emails = new ArrayList<>();
        Pattern emailPattern = Pattern.compile("(([A-Za-z0-9]+[A-Za-z0-9_.\\-]+[A-Za-z0-9]+)|([A-Za-z0-9]+))@([A-Za-z0-9]{2,}[.])+(ru|com|org|net)");

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

        if (!Pattern.matches(emailPattern.toString(), user.getEmail())){
            model.addAttribute("message", "Неккоректные данные");
            return "index";
        }

        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
            user.setUserpic(userService.loadUserpic(file));
        }

        if (!userService.addUser(user)){
            model.addAttribute("message", "Пользователь с таким E-mail уже существует");
            return "index";
        }

        for (User customUser : userService.findAllByAuthorityType(AuthorityType.CUSTOM)) {
            emails.add(customUser.getEmail());
        }

        model.addAttribute("emails", emails);
        model.addAttribute("message", "Вы успешно зарегистрировались, подтвердите свой аккаунт перейдя по ссылке");
        model.addAttribute("messages", "[]");
        model.addAttribute("profile", "null");

        return "index";
    }

    @GetMapping("loginError")
    public String loginError(Model model){
        addModelData(model);
        model.addAttribute("message", "Неверный E-mail или пароль, попробуйте ещё раз");
        return "index";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code){
        boolean isActivate = userService.activateUser(code);

        if (isActivate){
            model.addAttribute("message", "Вы успешно зарегистрировались");
        } else {
            model.addAttribute("message", "Код активации не найден!");
        }

        addModelData(model);

        return "index";
    }

    private void addModelData(Model model) {
        Map<Object, Object> data = new HashMap<>();
        List<String> emails = new ArrayList<>();

        for (User u : userService.findAllByAuthorityType(AuthorityType.CUSTOM)) {
            emails.add(u.getEmail());
        }

        model.addAttribute("frontendData", data);
        model.addAttribute("isDevMode", "dev".equals(profile));
        model.addAttribute("emails", emails);
        model.addAttribute("messages", "[]");
        model.addAttribute("profile", "null");
    }
}
