package com.example.socialNetwork.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    NEW_CUSTOMER, CUSTOMER, ADMINISTRATOR;

    @ Override
    public String getAuthority() {
        return name();
    }
}
