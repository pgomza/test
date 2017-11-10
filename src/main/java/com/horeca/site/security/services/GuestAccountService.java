package com.horeca.site.security.services;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.security.models.AbstractAccount;
import com.horeca.site.security.models.GuestAccount;
import com.horeca.site.security.repositories.GuestAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static com.horeca.site.security.OAuth2AuthorizationServerConfig.MOBILE_CLIENT_ID;

@Service
@Transactional
public class GuestAccountService extends AbstractAccountService<GuestAccount> {

    @Autowired
    private GuestAccountRepository repository;

    @Autowired
    private LoginService loginService;

    @Autowired
    private TokenStore tokenStore;

    @Override
    protected GuestAccountRepository getRepository() {
        return repository;
    }

    @Override
    public boolean exists(String login) {
        return loginService.exists(login);
    }

    @Override
    public GuestAccount get(String login) {
        GuestAccount account = getRepository().findOne(AbstractAccount.MOBILE_CLIENT_USERNAME_PREFIX + login);
        if (account == null) {
            throw new ResourceNotFoundException();
        }
        return account;
    }

    @Override
    public void delete(String login) {
        getRepository().delete(AbstractAccount.MOBILE_CLIENT_USERNAME_PREFIX + login);
    }

    public void registerGuest(Stay stay) {
        GuestAccount guestAccount = new GuestAccount(AbstractAccount.MOBILE_CLIENT_USERNAME_PREFIX + stay.getPin());
        save(guestAccount);
    }

    public void disableForStay(String pin) {
        GuestAccount account = get(AbstractAccount.MOBILE_CLIENT_USERNAME_PREFIX + pin);
        disable(account);
        Collection<OAuth2AccessToken> tokens =
                tokenStore.findTokensByClientIdAndUserName(MOBILE_CLIENT_ID, account.getUsername());
        tokens.forEach(tokenStore::removeAccessToken);
    }

    public void enableForStay(String pin) {
        GuestAccount account = get(AbstractAccount.MOBILE_CLIENT_USERNAME_PREFIX + pin);
        enable(account);
    }

    public void deleteForStay(String pin) {
        disableForStay(pin);
        GuestAccount account = get(AbstractAccount.MOBILE_CLIENT_USERNAME_PREFIX + pin);
        getRepository().delete(account);
    }
}
