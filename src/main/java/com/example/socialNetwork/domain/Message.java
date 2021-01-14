package com.example.socialNetwork.domain;

import com.example.socialNetwork.dto.MessageType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@ToString(of = {"id", "text", "creationDate"})
@EqualsAndHashCode(of = {"id"})
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.Id.class)
    private Long id;
    @JsonView(Views.IdName.class)
    private String text;

    @Column(updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonView(Views.FullMessage.class)
    private LocalDateTime creationDate;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @JsonView(Views.FullMessage.class)
    private User author;

    @JsonView(Views.FullMessage.class)
    private String filename;

    @JsonView(Views.FullMessage.class)
    private String link;
    @JsonView(Views.FullMessage.class)
    private String linkTitle;
    @JsonView(Views.FullMessage.class)
    private String linkDescription;
    @JsonView(Views.FullMessage.class)
    private String linkCover;

    @JsonView(Views.FullMessage.class)
    @Enumerated(EnumType.STRING)
    @Column(name = "message_type")
    private MessageType messageType;
}
