package com.horeca.site.websocket;

import org.apache.log4j.Logger;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.Optional;

public class WebSocketUpdatesHandler extends TextWebSocketHandler {

    private static Logger logger = Logger.getLogger(WebSocketUpdatesHandler.class);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {

    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // in this case only log the message
        Optional<OAuth2Authentication> authentication = getAuthentication(session);
        if (authentication.isPresent())
            logger.debug("Received websocket message: " + message + " from " + authentication.get().getPrincipal());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        session.close(CloseStatus.SERVER_ERROR);
    }

    private Optional<OAuth2Authentication> getAuthentication(WebSocketSession session) {
        Map<String, Object> attributes = session.getAttributes();
        OAuth2Authentication authentication = (OAuth2Authentication) attributes.get("authentication");
        return Optional.ofNullable(authentication);
    }
}
