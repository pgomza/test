package com.horeca.site.security.models;

import javax.persistence.Entity;

@Entity
public class GuestAccount extends AbstractAccount {

    public static final String USERNAME_PREFIX = "PIN_";

    GuestAccount() {
    }

    public GuestAccount(String username) {
        super(username);
    }

    @Override
    public String getUsernamePrefix() {
        return USERNAME_PREFIX;
    }

    public String getPin() {
        return getUsername().substring(getUsernamePrefix().length());
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getDefaultRole() {
        return "ROLE_GUEST";
    }
}
