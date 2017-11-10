package com.horeca.site.security.services;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.security.models.AbstractAccount;
import com.horeca.site.security.models.SalesmanAccount;
import com.horeca.site.security.repositories.SalesmanAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class SalesmanAccountService extends AbstractAccountService<SalesmanAccount> {

    @Autowired
    private SalesmanAccountRepository repository;

    @Autowired
    private LoginService loginService;

    @Override
    protected SalesmanAccountRepository getRepository() {
        return repository;
    }

    @Override
    public boolean exists(String login) {
        return loginService.exists(login);
    }

    @Override
    public SalesmanAccount get(String login) {
        SalesmanAccount account = getRepository().findOne(AbstractAccount.SALES_CLIENT_USERNAME_PREFIX + login);
        if (account == null) {
            throw new ResourceNotFoundException();
        }
        return account;
    }

    @Override
    public void delete(String login) {
        getRepository().delete(login);
    }

    public SalesmanAccount create(String login, String plainPassword) {
        String hashedPassword = PasswordHashingService.getHashedFromPlain(plainPassword);
        SalesmanAccount account = new SalesmanAccount(AbstractAccount.SALES_CLIENT_USERNAME_PREFIX + login,
                hashedPassword, Collections.singletonList(SalesmanAccount.DEFAULT_ROLE));
        return save(account);
    }
}
