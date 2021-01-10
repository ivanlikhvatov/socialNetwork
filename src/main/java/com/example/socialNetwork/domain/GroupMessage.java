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
    @JsonView(Views.FullMessage.class)
    @ElementCollection
    @CollectionTable(name = "addressees")
    private List<String> addressees;
}
