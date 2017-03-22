package com.horeca.config;

import com.horeca.site.models.stay.Stay;
import com.horeca.site.security.models.UserAccount;
import com.horeca.site.security.services.GuestAccountService;
import com.horeca.site.security.services.LoginService;
import com.horeca.site.security.services.UserAccountService;
import com.horeca.site.services.services.StayService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// responsible for performing various maintenance actions after
// the startup i.e. after the application context has been loaded
@Service
@Transactional
public class StartupRunner {

    private static final Logger logger = Logger.getLogger(StartupRunner.class);

    @Autowired
    private LoginService loginService;

    @Autowired
    private StayService stayService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private GuestAccountService guestAccountService;

    @EventListener(ContextRefreshedEvent.class)
    public void contextRefreshedEvent() {
        addDefaultAdmins();
        checkGuestAccountsForStays();
    }

    private void addDefaultAdmins() {
        String[] admins = new String[] { "admin1", "admin2" };
        for (int i = 0; i < admins.length; i++) {
            if (!loginService.exists(UserAccount.USERNAME_PREFIX + admins[i])) {
                List<String> roles = new ArrayList<>(Arrays.asList(UserAccount.DEFAULT_ROLE));
                String salt = BCrypt.gensalt(12);
                String hashed_password = BCrypt.hashpw("throdi" + i, salt);
                UserAccount account = new UserAccount(UserAccount.USERNAME_PREFIX + admins[i], hashed_password, new Long(i), roles);
                userAccountService.save(account);
            }
        }
    }

    private void checkGuestAccountsForStays() {
        Iterable<Stay> stays = stayService.getAll();
        for (Stay stay : stays) {
            if (!guestAccountService.existsForStay(stay.getPin()))
                guestAccountService.registerGuest(stay);
        }
    }
}
