package com.example.socialNetwork.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "group_message")
@Data
public class GroupMessage extends Message {
    @ManyToOne
    @JoinColumn(name = "group_id")
    @JsonView(Views.FullMessage.class)
    private Group group;
}
