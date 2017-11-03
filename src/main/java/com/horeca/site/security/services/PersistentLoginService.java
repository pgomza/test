package com.horeca.site.security.services;

import com.horeca.site.security.models.AbstractAccount;
import com.horeca.site.security.repositories.GuestAccountRepository;
import com.horeca.site.security.repositories.RootAccountRepository;
import com.horeca.site.security.repositories.SalesmanAccountRepository;
import com.horeca.site.security.repositories.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PersistentLoginService implements LoginService {

    @Autowired
    private GuestAccountRepository guestAccountRepository;
    @Autowired
    private UserAccountRepository userAccountRepository;
    @Autowired
    private SalesmanAccountRepository salesmanAccountRepository;
    @Autowired
    private RootAccountRepository rootAccountRepository;

    @Override
    public boolean exists(String username) {
        return loadUserByUsername(username) != null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails account = null;
        if (username.startsWith(AbstractAccount.MOBILE_CLIENT_USERNAME_PREFIX))
            account = guestAccountRepository.findOne(username);
        else if (username.startsWith(AbstractAccount.SALES_CLIENT_USERNAME_PREFIX))
            account = salesmanAccountRepository.findOne(username);
        else if (username.startsWith(AbstractAccount.PANEL_CLIENT_USERNAME_PREFIX)) {
            account = userAccountRepository.findOne(username);
            if (account == null) account = rootAccountRepository.findOne(username);
        }
        return account;
    }
}
