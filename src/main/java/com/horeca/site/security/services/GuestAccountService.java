package com.horeca.site.security.services;

import com.horeca.site.models.stay.Stay;
import com.horeca.site.security.OAuth2AuthorizationServerConfig;
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
    private TokenStore tokenStore;

    @Override
    protected GuestAccountRepository getRepository() {
        return repository;
    }

    @Override
    protected String loginToUsername(String login) {
        return AbstractAccount.MOBILE_CLIENT_USERNAME_PREFIX + login;
    }

    @Override
    protected String getOAuthClientId() {
        return OAuth2AuthorizationServerConfig.MOBILE_CLIENT_ID;
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
