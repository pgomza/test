package com.horeca.site.security.services;

import com.horeca.site.repositories.GuestAccountRepository;
import com.horeca.site.repositories.SalesmanAccountRepository;
import com.horeca.site.repositories.UserAccountRepository;
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
    private GuestAccountRepository guestAccountRepository;
    @Autowired
    private UserAccountRepository userAccountRepository;
    @Autowired
    private SalesmanAccountRepository salesmanAccountRepository;

    @Override
    public boolean exists(String username) {
        if (username.startsWith(GuestAccount.USERNAME_PREFIX))
            return guestAccountRepository.exists(username);
        else if (username.startsWith(UserAccount.USERNAME_PREFIX))
            return userAccountRepository.exists(username);
        else if (username.startsWith(SalesmanAccount.USERNAME_PREFIX))
            return salesmanAccountRepository.exists(username);
        else
            return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.startsWith(GuestAccount.USERNAME_PREFIX))
            return guestAccountRepository.findOne(username);
        else if (username.startsWith(UserAccount.USERNAME_PREFIX))
            return userAccountRepository.findOne(username);
        else if (username.startsWith(SalesmanAccount.USERNAME_PREFIX))
            return salesmanAccountRepository.findOne(username);
        else
            return null;
    }
}
