package com.horeca.site.models.accounts;

import javax.validation.constraints.NotNull;

public class UserAccountPOST extends AccountPOST {

    @NotNull
    private Long hotelId;

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }
}
