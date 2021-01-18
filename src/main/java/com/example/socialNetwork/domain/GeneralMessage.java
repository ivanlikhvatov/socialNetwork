package com.example.socialNetwork.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "general_message")
@Data
public class GeneralMessage extends Message {
}
