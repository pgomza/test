package com.horeca.site.websocket;

import com.horeca.site.security.models.UserAccount;
import org.apache.log4j.Logger;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;

public class DemoHandler extends TextWebSocketHandler {

    private static Logger logger = Logger.getLogger(WebSocketUpdatesHandler.class);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException, InterruptedException {
        OAuth2Authentication authentication = getAuthentication(session);
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserAccount) {
            UserAccount userAccount = (UserAccount) principal;
            String login = userAccount.getUsername().substring(userAccount.getUsernamePrefix().length());

            // for testing purposes
            int k = 0;
            for (int i = 0; i < 3; i++) {
                session.sendMessage(new TextMessage("UPDATE no. " + (k++) + " for " + login));
                Thread.sleep(10);
                session.sendMessage(new TextMessage("UPDATE no. " + (k++) + " for " + login));
                Thread.sleep(50);
                session.sendMessage(new TextMessage("UPDATE no. " + (k++) + " for " + login));
                Thread.sleep(20000);
            }
        }

        session.close(CloseStatus.SERVICE_RESTARTED);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // in this case only log the message
        OAuth2Authentication authentication = getAuthentication(session);
        logger.debug("Received websocket message: " + message + " from " + authentication.getPrincipal());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        session.close(CloseStatus.SERVER_ERROR);
    }

    private OAuth2Authentication getAuthentication(WebSocketSession session) {
        Map<String, Object> attributes = session.getAttributes();
        OAuth2Authentication authentication = (OAuth2Authentication) attributes.get("authentication");
        return authentication;
    }
}
