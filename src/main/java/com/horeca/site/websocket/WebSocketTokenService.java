package com.horeca.site.websocket;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.repositories.WebSocketTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class WebSocketTokenService {

    @Autowired
    private WebSocketTokenRepository repository;

    @Autowired
    private TokenStore tokenStore;

    public WebSocketTokenResponse generateFromOauthToken(String oauthToken) {
        if (oauthToken != null) {
            OAuth2Authentication authentication = tokenStore.readAuthentication(oauthToken);
            if (authentication != null) {
                String generatedToken = generateToken();
                WebSocketTokenMapping webSocketTokenMapping = new WebSocketTokenMapping(generatedToken, oauthToken);
                WebSocketTokenMapping saved = repository.save(webSocketTokenMapping);
                return new WebSocketTokenResponse(saved.getWebSocketToken());
            }
            throw new  BusinessRuleViolationException("Invalid token");
        }
        throw new BusinessRuleViolationException("ouathToken cannot be null");
    }

    public Optional<String> getOauthToken(String webSocketToken) {
        WebSocketTokenMapping tokenInfo = repository.findOne(webSocketToken);
        if (tokenInfo != null) {
            return Optional.ofNullable(tokenInfo.getOauthToken());
        }
        else {
            return Optional.empty();
        }
    }

    public boolean invalidate(String webSocketToken) {
        if (repository.findOne(webSocketToken) != null) {
            repository.delete(webSocketToken);
            return true;
        }
        return false;
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }
}
