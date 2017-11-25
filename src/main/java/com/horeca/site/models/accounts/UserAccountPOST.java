package com.horeca.site.models.accounts;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class UserAccountPOST extends AccountPOST {

    @NotEmpty
    private Long hotelId;

    @NotNull
    private Boolean fullAccess;

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public Boolean getFullAccess() {
        return fullAccess;
    }

    public void setFullAccess(Boolean fullAccess) {
        this.fullAccess = fullAccess;
    }
}
