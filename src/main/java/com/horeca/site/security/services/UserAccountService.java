package com.horeca.site.security.services;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.exceptions.UnauthorizedException;
import com.horeca.site.models.accounts.UserAccountView;
import com.horeca.site.security.models.AbstractAccount;
import com.horeca.site.security.models.UserAccount;
import com.horeca.site.security.repositories.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.horeca.site.security.OAuth2AuthorizationServerConfig.PANEL_CLIENT_ID;

@Service
@Transactional
public class UserAccountService extends AbstractAccountService<UserAccount> {

    @Autowired
    private UserAccountRepository repository;

    @Autowired
    private LoginService loginService;

    @Autowired
    private TokenStore tokenStore;

    @Override
    protected UserAccountRepository getRepository() {
        return repository;
    }

    @Override
    public boolean exists(String login) {
        return loginService.exists(AbstractAccount.PANEL_CLIENT_USERNAME_PREFIX + login);
    }

    @Override
    public UserAccount get(String login) {
        UserAccount account = getRepository().findOne(AbstractAccount.PANEL_CLIENT_USERNAME_PREFIX + login);
        if (account == null) {
            throw new ResourceNotFoundException();
        }
        return account;
    }

    @Override
    public void delete(String login) {
        getRepository().delete(AbstractAccount.PANEL_CLIENT_USERNAME_PREFIX + login);
    }

    @PostFilter("@accessChecker.checkForUserAccountFromCollection(authentication, filterObject)")
    public Set<UserAccount> getAll() {
        Set<UserAccount> userAccounts = new HashSet<>();
        for (UserAccount account : repository.findAll()) {
            userAccounts.add(account);
        }
        return userAccounts;
    }

    public Set<UserAccountView> getViews() {
        return getAll().stream().map(UserAccount::toView).collect(Collectors.toSet());
    }

    public void verifyAndChangePassword(String login, String currentPassword, String newPassword) {
        if (currentPassword.equals(newPassword)) {
            throw new BusinessRuleViolationException("The new password must be different from the current one");
        }

        UserAccount userAccount = get(login);
        if (!PasswordHashingService.checkIfPlainEqualToHashed(currentPassword, userAccount.getPassword())) {
            throw new UnauthorizedException("The provided currentPassword doesn't match the actual one");
        }

        changePassword(login, newPassword);
    }

    public void changePassword(String login, String newPassword) {
        if (!newPassword.matches(PASSWORD_REGEX)) {
            throw new BusinessRuleViolationException("The new password must contain at least 5 non-whitespace " +
                    "characters");
        }

        String hashedPassword = PasswordHashingService.getHashedFromPlain(newPassword);
        UserAccount userAccount = get(login);
        userAccount.setPassword(hashedPassword);
        save(userAccount);
    }

    public void disableAllInHotel(Long hotelId) {
        List<UserAccount> accountsInHotel = getRepository().findAllByHotelId(hotelId);
        accountsInHotel.forEach(this::disable);

        for (UserAccount account : accountsInHotel) {
            Collection<OAuth2AccessToken> tokens =
                    tokenStore.findTokensByClientIdAndUserName(PANEL_CLIENT_ID, account.getUsername());
            tokens.forEach(tokenStore::removeAccessToken);
        }
    }

    public void enableAllInHotel(Long hotelId) {
        List<UserAccount> accountsInHotel = getRepository().findAllByHotelId(hotelId);
        accountsInHotel.forEach(this::enable);
    }

    public void deleteAllInHotel(Long hotelId) {
        disableAllInHotel(hotelId);
        List<UserAccount> accountsInHotel = getRepository().findAllByHotelId(hotelId);
        getRepository().delete(accountsInHotel);
    }
}
