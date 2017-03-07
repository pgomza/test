package com.horeca.config;

import com.horeca.site.security.Account;
import com.horeca.site.security.LoginService;
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

    @EventListener(ContextRefreshedEvent.class)
    public void contextRefreshedEvent() {
        addRootUser();
    }

    // root user has permission to manage all users
    // he SHOULD NOT have access to anything else (especially
    // getting information any stay)
    private void addRootUser() {
        String hardcodedUsername = "ROOT";
        if (!loginService.isAlreadyPresent(Account.AUTH_PREFIX_USER + hardcodedUsername)) {
            List<String> roles = new ArrayList<>(Arrays.asList("ROLE_ROOT"));
            String salt = BCrypt.gensalt(12);
            String hashed_password = BCrypt.hashpw("throdi", salt);
            Account account = new Account(Account.AUTH_PREFIX_USER + hardcodedUsername, hashed_password, roles);
            loginService.saveUser(account);
        }
    }
}
