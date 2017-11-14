package com.horeca.site.security.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import java.util.Arrays;
import java.util.List;

@Entity
public class RootAccount extends AbstractAccount {

    public static final String DEFAULT_ROLE = "ROLE_ROOT";

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    RootAccount() {
    }

    public RootAccount(String username, String password) {
        super(username);
        this.password = password;
    }

    @Override
    public String getLogin() {
        return getUsername().substring(PANEL_CLIENT_USERNAME_PREFIX.length());
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return Arrays.asList(DEFAULT_ROLE, SalesmanAccount.DEFAULT_ROLE);
    }
}
