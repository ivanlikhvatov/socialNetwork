package com.example.socialNetwork.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "private_message")
@Data
public class PrivateMessage extends Message {
    @JsonView(Views.FullMessage.class)
    @ManyToOne
    @JoinColumn(name = "addressee_id")
    private User addressee;
}
