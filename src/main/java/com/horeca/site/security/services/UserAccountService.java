package com.horeca.site.security.services;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.UnauthorizedException;
import com.horeca.site.models.accounts.UserAccountView;
import com.horeca.site.security.OAuth2AuthorizationServerConfig;
import com.horeca.site.security.models.AbstractAccount;
import com.horeca.site.security.models.UserAccount;
import com.horeca.site.security.repositories.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.horeca.site.security.OAuth2AuthorizationServerConfig.PANEL_CLIENT_ID;

@Service
@Transactional
public class UserAccountService extends AbstractAccountService<UserAccount> {

    @Autowired
    private UserAccountRepository repository;

    @Autowired
    private TokenStore tokenStore;

    @Override
    protected UserAccountRepository getRepository() {
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

    @Override
    public boolean exists(String login) {
        return "current".equals(login) || super.exists(login);
    }

    public List<UserAccountView> getAllViews() {
        return getAll().stream().map(UserAccount::toView).collect(Collectors.toList());
    }

    public Page<UserAccountView> getAllViews(Pageable pageable) {
        List<UserAccountView> accountViews = getAll(pageable).getContent().stream()
                .map(UserAccount::toView)
                .collect(Collectors.toList());
        return new PageImpl<>(accountViews, pageable, getRepository().getTotalCount());
    }

    public List<UserAccountView> getAllViews(Long hotelId) {
        return getRepository().findAllByHotelId(hotelId).stream().map(UserAccount::toView).collect(Collectors.toList());
    }

    public Page<UserAccountView> getAllViews(Long hotelId, Pageable pageable) {
        List<UserAccount> byHotelId = getRepository().findAllByHotelId(hotelId);
        List<UserAccountView> accountViews = byHotelId.stream()
                .map(UserAccount::toView)
                .collect(Collectors.toList());
        return new PageImpl<>(accountViews, pageable, byHotelId.size());
    }

    public UserAccount create(String login, String password, boolean isPasswordAlreadyHashed, Long hotelId,
                              List<String> roles) {
        if (exists(login)) {
            throw new BusinessRuleViolationException("Such a user account already exists");
        }

        String hashedPassword;
        if (isPasswordAlreadyHashed) {
            hashedPassword = password;
        }
        else {
            hashedPassword = PasswordHashingService.getHashedFromPlain(password);
        }

        UserAccount account = new UserAccount(loginToUsername(login), hashedPassword, hotelId, roles);
        return save(account);
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
