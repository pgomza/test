package com.horeca.site.security.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.horeca.site.models.accounts.SalesmanAccountView;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Audited
public class SalesmanAccount extends AbstractAccount {

    public static final String ROLE_DEFAULT = "ROLE_SALESMAN";

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    @NotAudited
    @ElementCollection
    @CollectionTable(name = "SalesmanProfileData", joinColumns = @JoinColumn(name = "username"))
    @MapKeyColumn(name="name")
    @Column(name="value")
    private Map<String, String> profileData = new HashMap<>();

    SalesmanAccount() {
    }

    public SalesmanAccount(String username, String password) {
        this(username, password, Arrays.asList(ROLE_DEFAULT, UserAccount.ROLE_HOTEL_FULL),
                new HashMap<>());
    }

    public SalesmanAccount(String username, String password, List<String> roles) {
        this(username, password, roles, new HashMap<>());
    }

    public SalesmanAccount(String username, String password, Map<String, String> profileData) {
        this(username, password, Arrays.asList(ROLE_DEFAULT, UserAccount.ROLE_HOTEL_FULL),
                profileData);
    }

    public SalesmanAccount(String username, String password, List<String> roles, Map<String, String> profileData) {
        super(username);
        this.password = password;
        this.roles = roles;
        this.profileData = profileData;
    }

    @Override
    public String getLogin() {
        return getUsername().substring(SALES_CLIENT_USERNAME_PREFIX.length());
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public SalesmanAccountView toView() {
        SalesmanAccountView view = new SalesmanAccountView();
        view.setLogin(getLogin());
        view.setRoles(getRoles());
        view.setAccountNonExpired(isAccountNonExpired());
        view.setAccountNonLocked(isAccountNonLocked());
        view.setCredentialsNonExpired(isCredentialsNonExpired());
        view.setEnabled(isEnabled());
        view.setProfileData(getProfileData());
        return view;
    }
}
