package com.example.socialNetwork.repo;

import com.example.socialNetwork.domain.SocialUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialUserRepo extends JpaRepository<SocialUser, String> {
}
