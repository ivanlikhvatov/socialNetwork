package com.example.socialNetwork.service;

import com.example.socialNetwork.domain.AuthorityType;
import com.example.socialNetwork.domain.CustomUser;
import com.example.socialNetwork.domain.Role;
import com.example.socialNetwork.domain.User;
import com.example.socialNetwork.repo.CustomUserRepo;
import com.example.socialNetwork.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private CustomUserRepo customUserRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    MailSender mailSender;

    public boolean addUser(CustomUser user){
        CustomUser userFromDb = customUserRepo.findByEmail(user.getEmail());

        if (userFromDb != null){
            return false;
        }

        user.setId(UUID.randomUUID().toString());
        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));
        user.setAuthorityType(AuthorityType.CUSTOM);
        user.setActivationCode(UUID.randomUUID().toString());

        if (user.getGender().equals("муж")){
            user.setGender("male");
        } else {
            user.setGender("female");
        }

        userRepo.save(user);

        if (!StringUtils.isEmpty(user.getEmail())){
            String message = String.format(
                    "Здравствуйте, %s \n" + "Приветствуем Вас в нашей социальной сети. Пожалуйста посетите следующую ссылку: http://localhost:8080/activate/%s",
                    user.getUsername(),
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

//        if (newEmail != null){
//            user.setEmail(newEmail);
//        }

        customUserRepo.save(user);

        return true;
    }

    public List<User> findAllByAuthorityType(AuthorityType type) {
        return  userRepo.findAllByAuthorityType(type);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return customUserRepo.findByEmail(email);
    }
}
