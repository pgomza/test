package com.horeca.site.models.accounts;

import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@Audited
public class UserAccountPending extends AccountPending {

    @NotNull
    private Long hotelId;

    @NotEmpty
    private String redirectUrl;

    UserAccountPending() {}

    public UserAccountPending(String email, String password, Long hotelId, String secret, String redirectUrl) {
        super(email, password, secret);
        this.hotelId = hotelId;
        this.redirectUrl = redirectUrl;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
