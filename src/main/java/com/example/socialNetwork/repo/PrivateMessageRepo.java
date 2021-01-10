package com.example.socialNetwork.repo;

import com.example.socialNetwork.domain.PrivateMessage;
import com.example.socialNetwork.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivateMessageRepo extends JpaRepository<PrivateMessage, Long> {
//    Page<PrivateMessage> findAllByAddresseeOrAuthor(User addressee, User author, Pageable pageable);
    Page<PrivateMessage> findAllByAddresseeAndAuthorOrAuthorAndAddressee(User addressee1, User author1, User addressee2, User author2, Pageable pageable);
}
