package com.example.socialNetwork.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "social_usr")
@Data
public class SocialUser extends User implements OidcUser {
    @Override
    public Map<String, Object> getClaims() {
        return null;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return null;
    }

    @Override
    public OidcIdToken getIdToken() {
        return null;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }
}