package com.example.socialNetwork.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "chat_group")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.Id.class)
    private Long id;

    @JsonView(Views.IdName.class)
    private String name;

    @JsonView(Views.FullGroup.class)
    @ElementCollection
    @CollectionTable(name = "group_members")
    private List<String> members;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    @JsonView(Views.FullGroup.class)
    private User admin;

    @Column(updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonView(Views.FullGroup.class)
    private LocalDateTime creationDate;


}
