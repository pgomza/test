package com.horeca.site.security.services;

import com.horeca.site.security.models.GuestAccount;
import com.horeca.site.security.models.SalesmanAccount;
import com.horeca.site.security.models.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PersistentLoginService implements LoginService {

    @Autowired
    private GuestAccountService guestAccountService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private SalesmanAccountService salesmanAccountService;

    @Override
    public boolean exists(String username) {
        if (username.startsWith(GuestAccount.USERNAME_PREFIX))
            return guestAccountService.exists(username);
        else if (username.startsWith(UserAccount.USERNAME_PREFIX))
            return userAccountService.exists(username);
        else if (username.startsWith(SalesmanAccount.USERNAME_PREFIX))
            return salesmanAccountService.exists(username);
        else
            return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.startsWith(GuestAccount.USERNAME_PREFIX))
            return guestAccountService.get(username);
        else if (username.startsWith(UserAccount.USERNAME_PREFIX))
            return userAccountService.get(username);
        else if (username.startsWith(SalesmanAccount.USERNAME_PREFIX))
            return salesmanAccountService.get(username);
        else
            return null;
    }
}
