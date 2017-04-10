package com.horeca.site.security.models;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class UserAccountPending {

    @Id
    @Email
    private String email;

    @NotEmpty
    private String password;

    @NotNull
    private Long hotelId;

    @NotEmpty
    private String secret;

    @NotEmpty
    private String redirectUrl;

    UserAccountPending() {
    }

    public UserAccountPending(String email, String password, Long hotelId, String secret, String redirectUrl) {
        this.email = email;
        this.password = password;
        this.hotelId = hotelId;
        this.secret = secret;
        this.redirectUrl = redirectUrl;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public String getSecret() {
        return secret;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }
}
