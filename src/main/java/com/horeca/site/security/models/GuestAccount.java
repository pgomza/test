package com.horeca.site.security.models;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Entity
@Audited
public class GuestAccount extends AbstractAccount {

    public static final String ROLE_DEFAULT = "ROLE_GUEST";

    @NotAudited
    @ElementCollection
    @CollectionTable(name = "GuestProfileData", joinColumns = @JoinColumn(name = "username"))
    @MapKeyColumn(name="name")
    @Column(name="value")
    private Map<String, String> profileData = new HashMap<>();

    GuestAccount() {
    }

    public GuestAccount(String username) {
        this(username, new HashMap<>());
    }

    public GuestAccount(String username, Map<String, String> profileData) {
        super(username);
        this.profileData = profileData;
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
        return Collections.singletonList(ROLE_DEFAULT);
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
