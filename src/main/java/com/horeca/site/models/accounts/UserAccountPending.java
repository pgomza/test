package com.horeca.site.models.accounts;

import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@Audited
public class UserAccountPending extends AccountPending {

    @NotNull
    private Long hotelId;

    UserAccountPending() {}

    public UserAccountPending(String email, String password, Long hotelId, String secret) {
        super(email, password, secret);
        this.hotelId = hotelId;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }
}
