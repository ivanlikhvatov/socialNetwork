package com.example.socialNetwork.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Collection;


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
