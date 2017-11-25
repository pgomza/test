package com.horeca.site.security.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class RootAccount extends AbstractAccount {

    public static final String ROLE_DEFAULT = "ROLE_ROOT";

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotAudited
    @ElementCollection
    @CollectionTable(name = "RootProfileData", joinColumns = @JoinColumn(name = "username"))
    @MapKeyColumn(name="name")
    @Column(name="value")
    private Map<String, String> profileData = new HashMap<>();

    RootAccount() {
    }

    public RootAccount(String username, String password) {
        this(username, password, new HashMap<>());
    }

    public RootAccount(String username, String password, Map<String, String> profileData) {
        super(username);
        this.password = password;
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

    public List<String> getRoles() {
        return Arrays.asList(ROLE_DEFAULT, SalesmanAccount.ROLE_DEFAULT, UserAccount.ROLE_DEFAULT, UserAccount.ROLE_HOTEL_BASIC,
                UserAccount.ROLE_HOTEL_FULL);
    }

    @Override
    public Map<String, String> getProfileData() {
        return profileData;
    }

    @Override
    public void setProfileData(Map<String, String> profileData) {
        this.profileData = profileData;
    }
}
