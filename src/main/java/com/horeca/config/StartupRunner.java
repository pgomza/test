package com.horeca.config;

import com.horeca.site.services.StayInfoAsHtmlService;
import com.horeca.site.services.accounts.PasswordResetPendingService;
import com.horeca.site.services.cubilis.CubilisReservationService;
import com.horeca.site.websocket.WebSocketTokenService;
import com.openhtmltopdf.util.XRLog;
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
    private PasswordResetPendingService passwordResetPendingService;

    @Autowired
    private WebSocketTokenService webSocketTokenService;

    @Autowired
    private CubilisReservationService cubilisReservationService;

    @Autowired
    private StayInfoAsHtmlService stayInfoAsHtmlService;

    @EventListener(ContextRefreshedEvent.class)
    public void contextRefreshedEvent() {
        // turn off logging for the html-to-pdf conversion library
        XRLog.setLoggingEnabled(false);

        webSocketTokenService.deleteAll();
        cubilisReservationService.deleteOutdated();
        passwordResetPendingService.deleteAllInvalid();
        stayInfoAsHtmlService.deleteAllCodeImages();
    }
}
