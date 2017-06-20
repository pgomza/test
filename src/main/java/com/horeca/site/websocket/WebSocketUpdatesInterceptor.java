package com.horeca.site.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class WebSocketUpdatesInterceptor implements HandshakeInterceptor {

    private static final Pattern tokenPattern = Pattern.compile(".*token=([a-f0-9-]+).*");

    @Autowired
    private TokenStore tokenStore;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        URI requestURI = request.getURI();
        Optional<String> token = extractToken(requestURI.getQuery());
        if (token.isPresent()) {
            OAuth2Authentication authentication = tokenStore.readAuthentication(token.get());
            if (authentication != null) {
                attributes.put("authentication", authentication);
                return true;
            }
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception exception) {
    }

    private Optional<String> extractToken(String queryString) {
        Matcher matcher = tokenPattern.matcher(queryString);
        if (matcher.find() && matcher.groupCount() == 1) {
            return Optional.of(matcher.group(1));
        }
        else {
            return Optional.empty();
        }
    }
}
