package com.example.socialNetwork.repo;

import com.example.socialNetwork.domain.Group;
import com.example.socialNetwork.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepo extends JpaRepository<Group, Long> {
    //мб не будет работать
    List<Group> findAllByMembersContains(String id);
}
