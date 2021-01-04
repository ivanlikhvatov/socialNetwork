package com.example.socialNetwork.repo;

import com.example.socialNetwork.domain.AuthorityType;
import com.example.socialNetwork.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepo extends JpaRepository<User, String> {
    List<User> findAllByAuthorityType(AuthorityType type);
}
