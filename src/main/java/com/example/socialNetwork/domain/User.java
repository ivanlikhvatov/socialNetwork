package com.example.socialNetwork.domain;

import com.example.socialNetwork.dto.AuthorityType;
import com.example.socialNetwork.dto.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

@Data
@Entity
@Table(name = "usr")
public class User implements Serializable, UserDetails{
    @Id
    @JsonView(Views.IdName.class)
    private String id;

    @JsonView(Views.IdName.class)
    private String name;

    @JsonView(Views.FullProfile.class)
    private String userpic;

    @JsonView(Views.FullProfile.class)
    private String email;

    @JsonView(Views.FullProfile.class)
    private String gender;

    @JsonView(Views.FullProfile.class)
    private String locale;

    @JsonView(Views.FullProfile.class)
    private boolean active;

    @JsonView(Views.FullProfile.class)
    private boolean online;

    @JsonView(Views.FullProfile.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastVisit;

    @JsonView(Views.FullProfile.class)
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @JsonView(Views.FullProfile.class)
    @Enumerated(EnumType.STRING)
    @Column(name = "authority_type")
    private AuthorityType authorityType;

    @JsonView(Views.FullProfile.class)
    private boolean nonLocked;

//    @OneToOne(fetch = FetchType.EAGER, orphanRemoval = true)
//    @JoinColumn(name="user_id")
//    private User user;

    @JsonView(Views.FullProfile.class)
    public boolean isAdmin(){
        return roles.contains(Role.ADMIN);
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return nonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }


}
