package com.horeca.site.websocket;

import org.hibernate.validator.constraints.NotEmpty;

public class WebSocketTokenResponse {

    @NotEmpty
    private String token;

    public WebSocketTokenResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
