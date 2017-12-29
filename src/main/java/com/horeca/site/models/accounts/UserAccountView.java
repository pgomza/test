package com.horeca.site.models.accounts;

import java.util.List;

public class UserAccountView extends AccountView {

    private Long hotelId;

    public UserAccountView(String login, List<String> roles, boolean enabled, Long hotelId) {
        super(Type.USER, login, roles, enabled);
        this.hotelId = hotelId;
    }

    public Long getHotelId() {
        return hotelId;
    }
}
