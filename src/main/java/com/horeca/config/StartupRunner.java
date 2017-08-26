package com.horeca.config;

import com.horeca.site.repositories.UserAccountRepository;
import com.horeca.site.security.models.UserAccount;
import com.horeca.site.security.services.UserAccountTempTokenService;
import com.horeca.site.services.HotelService;
import com.horeca.site.services.cubilis.CubilisReservationService;
import com.horeca.site.websocket.WebSocketTokenService;
import com.openhtmltopdf.util.XRLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private HotelService hotelService;

    @EventListener(ContextRefreshedEvent.class)
    public void contextRefreshedEvent() {
        // turn off logging for the html-to-pdf conversion library
        XRLog.setLoggingEnabled(false);

        userAccountTempTokenService.deleteInvalidTokens();
        webSocketTokenService.deleteAll();
        cubilisReservationService.deleteOutdated();
        ensureEnoughDataInActiveHotels();
    }

    private void ensureEnoughDataInActiveHotels() {
        Set<UserAccount> userAccounts = new HashSet<>();
        userAccountRepository.findAll().forEach(userAccounts::add);
        Set<Long> activeHotelIds = userAccounts.stream()
                .map(UserAccount::getHotelId)
                .filter(id -> id != null && id > 0L)
                .collect(Collectors.toSet());
        activeHotelIds.forEach(id -> hotelService.ensureEnoughInfoAboutHotel(id));
    }
}
