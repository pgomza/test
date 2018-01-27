package com.horeca.site.models.accounts;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.Collections;

@Entity
public class UserAccountPending extends AccountPending {

    @NotNull
    private Long hotelId;

    @NotNull
    private Boolean fullAccess;

    UserAccountPending() {}

    public UserAccountPending(String email, String password, String secret, Long hotelId, Boolean fullAccess) {
        super(email, password, secret);
        this.hotelId = hotelId;
        this.fullAccess = fullAccess;
    }

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

    public UserAccountView toView() {
        return new UserAccountView(getEmail(), Collections.emptyList(), false, getHotelId());
    }
}
