package com.horeca.site.security.models;

import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import java.util.Arrays;
import java.util.Collection;

@Entity
@Audited
public class GuestAccount extends AbstractAccount {

    public static final String DEFAULT_ROLE = "ROLE_GUEST";

    GuestAccount() {
    }

    public GuestAccount(String username) {
        super(username);
    }

    public String getPin() {
        return getLogin();
    }

    @Override
    public String getLogin() {
        return getUsername().substring(MOBILE_CLIENT_USERNAME_PREFIX.length());
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public Collection<String> getRoles() {
        return Arrays.asList(DEFAULT_ROLE);
    }
}
