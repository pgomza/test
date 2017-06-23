package com.horeca.site.websocket;

import com.horeca.site.exceptions.UnauthorizedException;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "hotels")
@RestController
@RequestMapping("/api/updates")
public class WebSocketUpdatesController {

    @Autowired
    private WebSocketTokenService tokenService;

    @Autowired
    private TokenStore tokenStore;

    @RequestMapping(value = "/token", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public WebSocketTokenResponse exchangeOauthToken(Authentication authentication) {
        if (authentication != null) {
            OAuth2AccessToken accessToken = tokenStore.getAccessToken((OAuth2Authentication) authentication);
            return tokenService.generateFromOauthToken(accessToken.getValue());
        }
        throw new UnauthorizedException();
    }
}
