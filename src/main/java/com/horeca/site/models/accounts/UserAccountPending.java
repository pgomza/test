package com.horeca.site.models.accounts;

import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@Audited
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
}
