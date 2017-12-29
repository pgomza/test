package com.horeca.site.models.accounts;

import org.hibernate.validator.constraints.NotEmpty;

public class UserAccountPOST extends AccountPOST {

    @NotEmpty
    private Long hotelId;

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }
}
