package com.example.socialNetwork.repo;

import com.example.socialNetwork.domain.GroupMessage;
import com.example.socialNetwork.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupMessageRepo extends JpaRepository<GroupMessage, Long> {
//    Page<GroupMessage> findAllByAddresseesOrAuthor(List<User> addressee, User author, Pageable pageable);
}
