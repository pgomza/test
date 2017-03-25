package com.horeca.site.security.models;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class UserAccountTempTokenResponse {

    @NotEmpty
    private String tempToken;

    @NotNull
    private Long hotelId;

    @NotNull
    private Integer expiresIn;

    public UserAccountTempTokenResponse(String tempToken, Long hotelId, Integer expiresIn) {
        this.tempToken = tempToken;
        this.hotelId = hotelId;
        this.expiresIn = expiresIn;
    }

    public String getTempToken() {
        return tempToken;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }
}
