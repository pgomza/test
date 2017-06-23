package com.horeca.site.websocket;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class WebSocketTokenMapping {

    @Id
    private String webSocketToken;

    @NotEmpty
    private String oauthToken;

    WebSocketTokenMapping() {
    }

    public WebSocketTokenMapping(String webSocketToken, String oauthToken) {
        this.webSocketToken = webSocketToken;
        this.oauthToken = oauthToken;
    }

    public String getWebSocketToken() {
        return webSocketToken;
    }

    public void setWebSocketToken(String webSocketToken) {
        this.webSocketToken = webSocketToken;
    }

    public String getOauthToken() {
        return oauthToken;
    }

    public void setOauthToken(String oauthToken) {
        this.oauthToken = oauthToken;
    }
}
