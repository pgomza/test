package com.horeca.site.security.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.envers.Audited;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import java.util.List;

@Entity
@Audited
public class SalesmanAccount extends AbstractAccount {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    SalesmanAccount() {
    }

    public SalesmanAccount(String username, String password, List<String> roles) {
        super(username);
        this.password = password;
        this.roles = roles;
    }

    @Override
    public String getUsernamePrefix() {
        return AbstractAccount.SALES_CLIENT_USERNAME_PREFIX;
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
}
