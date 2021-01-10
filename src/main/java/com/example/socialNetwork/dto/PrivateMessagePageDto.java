package com.example.socialNetwork.dto;

import com.example.socialNetwork.domain.GeneralMessage;
import com.example.socialNetwork.domain.PrivateMessage;
import com.example.socialNetwork.domain.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@JsonView(Views.FullMessage.class)
public class PrivateMessagePageDto {
    private List<PrivateMessage> messages;
    private int currentPage;
    private int totalPages;
}
