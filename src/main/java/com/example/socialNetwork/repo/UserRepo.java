package com.example.socialNetwork.repo;

import com.example.socialNetwork.dto.AuthorityType;
import com.example.socialNetwork.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepo extends JpaRepository<User, String> {
    List<User> findAllByAuthorityType(AuthorityType type);
    List<User> findAllByAuthorityTypeAndEmailContaining(AuthorityType type, String email);

    User findByEmail(String email);
}
