package com.horeca.site.security.models;

import com.horeca.site.models.hotel.Hotel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Set;

@Entity
public class UserAccountTempToken {

    @Id
    private String token;

    @ManyToOne(optional = false)
    private Hotel hotel;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles;

    @NotNull
    private Timestamp createdAt;

    @NotNull
    private Timestamp expiresAt;

    UserAccountTempToken() {
    }

    public UserAccountTempToken(String token, Hotel hotel, Set<String> roles, Timestamp createdAt, Timestamp expiresAt) {
        this.token = token;
        this.hotel = hotel;
        this.roles = roles;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Timestamp expiresAt) {
        this.expiresAt = expiresAt;
    }
}
