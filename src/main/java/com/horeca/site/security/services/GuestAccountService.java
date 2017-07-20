package com.horeca.site.security.services;

import com.horeca.site.models.stay.Stay;
import com.horeca.site.repositories.GuestAccountRepository;
import com.horeca.site.security.models.GuestAccount;
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

    public void registerGuest(Stay stay) {
        GuestAccount guestAccount = new GuestAccount(GuestAccount.USERNAME_PREFIX + stay.getPin());
        save(guestAccount);
    }

    public void disableForStay(String pin) {
        GuestAccount account = get(GuestAccount.USERNAME_PREFIX + pin);
        disable(account);
        Collection<OAuth2AccessToken> tokens =
                tokenStore.findTokensByClientIdAndUserName(MOBILE_CLIENT_ID, account.getUsername());
        tokens.forEach(tokenStore::removeAccessToken);
    }
}
