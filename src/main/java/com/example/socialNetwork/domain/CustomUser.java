package com.example.socialNetwork.domain;

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
    private String password;
    private String activationCode;
}
