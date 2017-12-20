package com.horeca.site.security.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.horeca.site.models.accounts.UserAccountView;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Audited
public class UserAccount extends AbstractAccount {

    public static final String ROLE_DEFAULT = "ROLE_USER";
    public static final String ROLE_HOTEL_FULL = "ROLE_HOTEL_FULL";

    private Long hotelId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    @NotAudited
    @ElementCollection
    @CollectionTable(name = "UserProfileData", joinColumns = @JoinColumn(name = "username"))
    @MapKeyColumn(name="name")
    @Column(name="value")
    private Map<String, String> profileData = new HashMap<>();

    UserAccount() {
    }

    public UserAccount(String username, String password, Long hotelId) {
        this(username, password, hotelId, Arrays.asList(ROLE_DEFAULT), new HashMap<>());
    }

    public UserAccount(String username, String password, Long hotelId, List<String> roles) {
        this(username, password, hotelId, roles, new HashMap<>());
    }

    public UserAccount(String username, String password, Long hotelId, Map<String, String> profileData) {
        this(username, password, hotelId, Arrays.asList(ROLE_DEFAULT), profileData);
    }

    public UserAccount(String username, String password, Long hotelId, List<String> roles, Map<String, String> profileData) {
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
        view.setProfileData(getProfileData());
        return view;
    }
}
