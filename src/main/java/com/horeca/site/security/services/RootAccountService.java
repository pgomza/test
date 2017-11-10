package com.horeca.site.security.services;

import com.horeca.site.exceptions.ResourceNotFoundException;
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

    @Autowired
    private LoginService loginService;

    @Override
    protected CrudRepository<RootAccount, String> getRepository() {
        return repository;
    }

    @Override
    public boolean exists(String login) {
        return loginService.exists(AbstractAccount.PANEL_CLIENT_USERNAME_PREFIX + login);
    }

    @Override
    public RootAccount get(String login) {
        RootAccount account = getRepository().findOne(AbstractAccount.PANEL_CLIENT_USERNAME_PREFIX + login);
        if (account == null) {
            throw new ResourceNotFoundException();
        }
        return account;
    }

    @Override
    public void delete(String login) {
        getRepository().delete(AbstractAccount.PANEL_CLIENT_USERNAME_PREFIX + login);
    }
}
