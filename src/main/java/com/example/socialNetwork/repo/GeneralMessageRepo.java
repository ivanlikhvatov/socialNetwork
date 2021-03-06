package com.example.socialNetwork.repo;

import com.example.socialNetwork.domain.GeneralMessage;
import com.example.socialNetwork.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeneralMessageRepo extends JpaRepository<GeneralMessage, Long> {
    Page<GeneralMessage> findAll(Pageable pageable);
}
