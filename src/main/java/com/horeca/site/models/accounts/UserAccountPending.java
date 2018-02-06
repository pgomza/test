package com.horeca.site.models.accounts;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.Collections;

@Entity
public class UserAccountPending extends AccountPending {

    private Long hotelId;

    private String hotelName;

    private String hotelAddress;

    @NotNull
    private Boolean fullAccess;

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getHotelAddress() {
        return hotelAddress;
    }

    public void setHotelAddress(String hotelAddress) {
        this.hotelAddress = hotelAddress;
    }

    public Boolean getFullAccess() {
        return fullAccess;
    }

    public void setFullAccess(Boolean fullAccess) {
        this.fullAccess = fullAccess;
    }

    public UserAccountView toView() {
        return new UserAccountView(getEmail(), Collections.emptyList(), false, getHotelId());
    }
}
