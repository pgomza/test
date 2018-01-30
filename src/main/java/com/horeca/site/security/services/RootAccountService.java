package com.horeca.site.security.services;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.security.OAuth2AuthorizationServerConfig;
import com.horeca.site.security.models.AbstractAccount;
import com.horeca.site.security.models.RootAccount;
import com.horeca.site.security.repositories.RootAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RootAccountService extends AbstractAccountService<RootAccount> {

    @Autowired
    private RootAccountRepository repository;

    @Override
    protected RootAccountRepository getRepository() {
        return repository;
    }

    @Override
    protected String loginToUsername(String login) {
        return AbstractAccount.PANEL_CLIENT_USERNAME_PREFIX + login;
    }

    @Override
    protected String getOAuthClientId() {
        return OAuth2AuthorizationServerConfig.PANEL_CLIENT_ID;
    }

    public RootAccount create(String login, String password, boolean isPasswordAlreadyHashed) {
        if (exists(login)) {
            throw new BusinessRuleViolationException("Such a root account already exists");
        }

        String hashedPassword;
        if (isPasswordAlreadyHashed) {
            hashedPassword = password;
        }
        else {
            hashedPassword = PasswordHashingService.getHashedFromPlain(password);
        }

        RootAccount account = new RootAccount(loginToUsername(login), hashedPassword);
        return save(account);
    }
}
