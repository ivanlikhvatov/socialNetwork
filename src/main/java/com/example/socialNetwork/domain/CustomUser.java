package com.example.socialNetwork.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "custom_usr")
public class CustomUser extends User{
    @JsonView(Views.SecureProfile.class)
    private String password;
    @JsonView(Views.SecureProfile.class)
    private String activationCode;
}
