package com.horeca.site.security.models;

import javax.validation.constraints.NotNull;

public class UserAccountTempTokenRequest {

    @NotNull
    private Long hotelId;

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }
}
