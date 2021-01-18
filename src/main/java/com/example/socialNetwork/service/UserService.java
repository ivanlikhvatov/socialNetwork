package com.example.socialNetwork.service;

import com.example.socialNetwork.domain.Views;
import com.example.socialNetwork.dto.AuthorityType;
import com.example.socialNetwork.domain.CustomUser;
import com.example.socialNetwork.dto.EventType;
import com.example.socialNetwork.dto.ObjectType;
import com.example.socialNetwork.dto.Role;
import com.example.socialNetwork.domain.User;
import com.example.socialNetwork.repo.CustomUserRepo;
import com.example.socialNetwork.repo.UserRepo;
import com.example.socialNetwork.util.WsSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService{
    @Value("${upload.path}")
    private String uploadPath;

    private final FindByIndexNameSessionRepository findByIndexNameSessionRepository;
    private final BiConsumer<EventType, String> wsSender;
    private final CustomUserRepo customUserRepo;
    private final UserRepo userRepo;
    private final MailSender mailSender;

    @Autowired
    public UserService(FindByIndexNameSessionRepository findByIndexNameSessionRepository, CustomUserRepo customUserRepo, UserRepo userRepo, MailSender mailSender, WsSender wsSender) {
        this.findByIndexNameSessionRepository = findByIndexNameSessionRepository;
        this.customUserRepo = customUserRepo;
        this.userRepo = userRepo;
        this.mailSender = mailSender;
        this.wsSender = wsSender.getSender(ObjectType.LOCKED, Views.FullProfile.class);
    }

    public boolean addUser(CustomUser user){
        user.setId(UUID.randomUUID().toString());
        user.setActive(false);
        user.setNonLocked(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setAuthorityType(AuthorityType.CUSTOM);
        user.setActivationCode(UUID.randomUUID().toString());

        if (user.getGender().equals("муж")){
            user.setGender("male");
        } else {
            user.setGender("female");
        }

        if (customUserRepo.findByEmail(user.getEmail()) == null){
            userRepo.save(user);
        } else {
            return false;
        }

        if (!StringUtils.isEmpty(user.getEmail())){
            String message = String.format(
                    "Здравствуйте, %s \n" + "Приветствуем Вас в нашей социальной сети. Пожалуйста посетите следующую ссылку: http://localhost:8080/activate/%s",
                    user.getName(),
                    user.getActivationCode()
            );

            mailSender.send(user.getEmail(), "Activation Code", message);
        }

        return true;
    }

    public boolean activateUser(String code) {
        CustomUser user = customUserRepo.findByActivationCode(code);

        if (user == null) {
            return false;
        }

        user.setActivationCode(null);
        user.setActive(true);

        customUserRepo.save(user);

        return true;
    }

    public void saveUser(User user, Map<String, String> form) {
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        user.getRoles().clear();

        for (String key : form.keySet()){
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }

        String locked = form.get("locked");

        if (locked != null && locked.equals("true")){
            user.setNonLocked(false);
            if (!StringUtils.isEmpty(user.getEmail())){
                String message = String.format(
                        "Здравствуйте, %s \n" + "Ваш аккаунт был заблокирован по причине нарушения правил нашего чата",
                        user.getName()
                );

                mailSender.send(user.getEmail(), "Activation Code", message);
            }

            findByIndexNameSessionRepository.findByPrincipalName(user.getId())
                    .keySet()
                    .forEach(session -> findByIndexNameSessionRepository.deleteById(session.toString()));

            wsSender.accept(EventType.LOGOUT, user.getId());
        }

        if (locked == null){
            if (!user.isAccountNonLocked() && !StringUtils.isEmpty(user.getEmail())){
                String message = String.format(
                        "Здравствуйте, %s \n" + "Ваш аккаунт разблокирован, пожалуйста соблюдайте наши правила",
                        user.getName()
                );

                mailSender.send(user.getEmail(), "Activation Code", message);
            }

            user.setNonLocked(true);
        }

        userRepo.save(user);
    }

    public List<User> findAllByAuthorityType(AuthorityType type) {
        return  userRepo.findAllByAuthorityType(type);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        CustomUser user = customUserRepo.findByEmail(email);

        if (user.isAccountNonLocked() && user.isEnabled()){
            user.setOnline(true);
            userRepo.save(user);
        }

        return user;
    }

    public String loadUserpic(MultipartFile file) {
        File uploadDir = new File(uploadPath);

        if (!uploadDir.exists()){
            uploadDir.mkdir();
        }
        String uuidFile = UUID.randomUUID().toString();
        String resultFilename = uuidFile + "." + file.getOriginalFilename();

        try {
            file.transferTo(new File(uploadPath + "/" + resultFilename));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultFilename;
    }

    public List<User> findAllByAuthorityTypeAndEmailContaining(String authorityType, String email) {
        return userRepo.findAllByAuthorityTypeAndEmailContaining(AuthorityType.valueOf(authorityType), email);
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public List<User> findActive(){
        List<User> users = new ArrayList<>();

        for (User user : userRepo.findAll()) {
            if (!findByIndexNameSessionRepository.findByPrincipalName(user.getId()).isEmpty()){
                users.add(user);
            }

        }

        return users;
    }

    public User findById(String id){
        return userRepo.findById(id).orElseGet(User::new);
    }
}
