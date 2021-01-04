package com.example.socialNetwork.repo;

import com.example.socialNetwork.domain.CustomUser;
import com.example.socialNetwork.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomUserRepo extends JpaRepository<CustomUser, String> {
    CustomUser findByActivationCode(String code);
    CustomUser findByEmail(String email);
}
