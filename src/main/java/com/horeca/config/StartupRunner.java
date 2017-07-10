package com.horeca.config;

import com.horeca.site.security.services.UserAccountTempTokenService;
import com.horeca.site.services.CubilisReservationService;
import com.horeca.site.websocket.WebSocketTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// responsible for performing various maintenance actions after
// the startup i.e. after the application context has been loaded
@Service
@Transactional
public class StartupRunner {

    @Autowired
    private UserAccountTempTokenService userAccountTempTokenService;

    @Autowired
    private WebSocketTokenService webSocketTokenService;

    @Autowired
    private CubilisReservationService cubilisReservationService;

    @EventListener(ContextRefreshedEvent.class)
    public void contextRefreshedEvent() {
        userAccountTempTokenService.deleteInvalidTokens();
        webSocketTokenService.deleteAll();
        cubilisReservationService.deleteOutdated();
    }
}
