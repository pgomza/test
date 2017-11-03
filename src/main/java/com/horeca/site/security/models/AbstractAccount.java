package com.horeca.site.security.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@MappedSuperclass
public abstract class AbstractAccount implements UserDetails {

    public static final String MOBILE_CLIENT_USERNAME_PREFIX = "PIN_";
    public static final String PANEL_CLIENT_USERNAME_PREFIX = "USER_";
    public static final String SALES_CLIENT_USERNAME_PREFIX = "SALESMAN_";

    @Id
    private String username;

    private boolean accountNonExpired = true;

    private boolean accountNonLocked = true;

    private boolean credentialsNonExpired = true;

    private boolean enabled = true;

    AbstractAccount() {
    }

    public AbstractAccount(String username) {
        if (!username.startsWith(getUsernamePrefix())) {
            username = getUsernamePrefix() + username;
        }
        this.username = username;
    }

    public abstract String getUsernamePrefix();

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public abstract String getPassword();

    public abstract Collection<String> getRoles();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (String role : getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return Collections.unmodifiableCollection(authorities);
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
