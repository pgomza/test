package com.horeca.site.security.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.horeca.site.models.accounts.UserAccountView;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import java.util.*;

@Entity
public class UserAccount extends AbstractAccount {

    public static final String USERNAME_PREFIX = "USER_";

    private Long hotelId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    UserAccount() {
    }

    public UserAccount(String username, String password, Long hotelId) {
        super(username);
        this.password = password;
        this.hotelId = hotelId;
        ensureRolesContainDefaultRole();
    }

    public UserAccount(String username, String password, Long hotelId, List<String> roles) {
        super(username);
        this.password = password;
        this.hotelId = hotelId;
        this.roles = roles;
        ensureRolesContainDefaultRole();
    }

    @Override
    public String getUsernamePrefix() {
        return USERNAME_PREFIX;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getDefaultRole() {
        return "ROLE_USER";
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
        ensureRolesContainDefaultRole();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>(roles.size());
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return Collections.unmodifiableCollection(authorities);
    }

    private void ensureRolesContainDefaultRole() {
        if (this.roles == null)
            this.roles = new ArrayList<>(Arrays.asList(getDefaultRole()));
        else if (!this.roles.contains(getDefaultRole()))
            this.roles.add(getDefaultRole());
    }

    public UserAccountView toView() {
        UserAccountView view = new UserAccountView();
        view.setLogin(getUsername().substring(getUsernamePrefix().length()));
        view.setHotelId(getHotelId());
        view.setRoles(getRoles());
        view.setAccountNonExpired(isAccountNonExpired());
        view.setAccountNonLocked(isAccountNonLocked());
        view.setCredentialsNonExpired(isCredentialsNonExpired());
        view.setEnabled(isEnabled());
        return view;
    }
}
