package com.horeca.site.security.services;

import com.horeca.site.security.models.AbstractAccount;
import com.horeca.site.security.models.RootAccount;
import com.horeca.site.security.repositories.RootAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RootAccountService extends AbstractAccountService<RootAccount> {

    @Autowired
    private RootAccountRepository repository;

    @Override
    protected CrudRepository<RootAccount, String> getRepository() {
        return repository;
    }

    @Override
    protected String loginToUsername(String login) {
        return AbstractAccount.PANEL_CLIENT_USERNAME_PREFIX + login;
    }
}
