package com.horeca.site.security.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.horeca.site.models.accounts.UserAccountView;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Audited
public class UserAccount extends AbstractAccount {

    public static final String DEFAULT_ROLE = "ROLE_ADMIN";

    private Long hotelId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    @ElementCollection
    @CollectionTable(name = "UserProfileData", joinColumns = @JoinColumn(name = "username"))
    @MapKeyColumn(name="name")
    @Column(name="value")
    private Map<String, String> profileData = new HashMap<>();

    UserAccount() {
    }

    public UserAccount(String username, Long hotelId, String password) {
        this(username, hotelId, password, Collections.singletonList(DEFAULT_ROLE), new HashMap<>());
    }

    public UserAccount(String username, Long hotelId, String password, List<String> roles) {
        this(username, hotelId, password, roles, new HashMap<>());
    }

    public UserAccount(String username, Long hotelId, String password, Map<String, String> profileData) {
        this(username, hotelId, password, Collections.singletonList(DEFAULT_ROLE), profileData);
    }

    public UserAccount(String username, Long hotelId, String password, List<String> roles, Map<String, String> profileData) {
        super(username);
        this.hotelId = hotelId;
        this.password = password;
        this.roles = roles;
        this.profileData = profileData;
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
    }

    @Override
    public Map<String, String> getProfileData() {
        return profileData;
    }

    @Override
    public void setProfileData(Map<String, String> profileData) {
        this.profileData = profileData;
    }

    public UserAccountView toView() {
        UserAccountView view = new UserAccountView();
        view.setLogin(getLogin());
        view.setHotelId(getHotelId());
        view.setRoles(getRoles());
        view.setAccountNonExpired(isAccountNonExpired());
        view.setAccountNonLocked(isAccountNonLocked());
        view.setCredentialsNonExpired(isCredentialsNonExpired());
        view.setEnabled(isEnabled());
        return view;
    }
}
