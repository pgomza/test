package com.horeca.site.websocket;

import com.horeca.site.models.updates.ChangeInHotelEvent;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class WebSocketUpdatesService implements ApplicationListener<ChangeInHotelEvent> {

    private static final Logger logger = Logger.getLogger(WebSocketUpdatesService.class);

    public static final String UPDATE_MESSAGE_TEXT = "UPDATE";
    private final Map<Long, Set<WebSocketSession>> hotelToSessions = new HashMap<>();

    public synchronized void registerSessionForHotel(Long hotelId, WebSocketSession session) {
        Set<WebSocketSession> existingSessions = hotelToSessions.getOrDefault(hotelId, new HashSet<>());
        existingSessions.add(session);
        hotelToSessions.put(hotelId, existingSessions);
    }

    public synchronized void deregisterSession(WebSocketSession session) {
        // since the number of hotels should not be very big, this is an acceptable solution
        for (Long hotelId : hotelToSessions.keySet()) {
            Set<WebSocketSession> sessionSet = hotelToSessions.get(hotelId);
            if (sessionSet.contains(session)) {
                sessionSet.remove(session);
                hotelToSessions.put(hotelId, sessionSet);
            }
        }
    }

    @Override
    public void onApplicationEvent(ChangeInHotelEvent event) {
        handleChangeInHotel(event.getHotelId());
    }

    private void handleChangeInHotel(Long hotelId) {
        Set<WebSocketSession> sessions = hotelToSessions.get(hotelId);
        if (sessions != null) {
            sessions.forEach(s -> {
                try {
                    s.sendMessage(new TextMessage(UPDATE_MESSAGE_TEXT));
                } catch (IOException e) {
                    logger.error("Exception while sending " + UPDATE_MESSAGE_TEXT + " to hotelId: " + hotelId +
                            " sessionId: " + s.getId());
                    logger.error("Exception's message: " + e.getMessage());
                    logger.error("Exception's cause: " + e.getCause());
                }
            });
        }
    }
}
