package com.horeca.site.models.accounts;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.horeca.site.models.hotel.Hotel;
import org.joda.time.LocalDateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
public class UserAccountTempToken {

    @Id
    private String token;

    @ManyToOne(optional = false)
    private Hotel hotel;

    @ElementCollection(fetch = FetchType.LAZY)
    private Set<String> roles;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime expiresAt;

    UserAccountTempToken() {
    }

    public UserAccountTempToken(String token, Hotel hotel, Set<String> roles, LocalDateTime createdAt, LocalDateTime expiresAt) {
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}
