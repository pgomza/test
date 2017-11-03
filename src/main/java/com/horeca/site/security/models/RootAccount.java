package com.horeca.site.security.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import java.util.Collections;
import java.util.List;

@Entity
public class RootAccount extends AbstractAccount {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    RootAccount() {
    }

    public RootAccount(String username, String password) {
        super(username);
        this.password = password;
    }

    @Override
    public String getUsernamePrefix() {
        return AbstractAccount.PANEL_CLIENT_USERNAME_PREFIX;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return Collections.singletonList("ROLE_ROOT");
    }
}
