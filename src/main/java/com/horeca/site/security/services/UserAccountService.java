package com.horeca.site.security.services;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.UnauthorizedException;
import com.horeca.site.models.accounts.UserAccountView;
import com.horeca.site.security.models.UserAccount;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserAccountService extends AbstractAccountService<UserAccount> {

    @PostFilter("@accessChecker.checkForUserAccountFromCollection(authentication, filterObject)")
    public Set<UserAccount> getAll() {
        Set<UserAccount> userAccounts = new HashSet<>();
        for (UserAccount account : repository.findAll()) {
            userAccounts.add(account);
        }
        return userAccounts;
    }

    public Set<UserAccountView> getViews() {
        return getAll().stream().map(userAccount -> userAccount.toView()).collect(Collectors.toSet());
    }

    public void changePassword(String username, String currentPassword, String newPassword) {
        if (currentPassword.equals(newPassword)) {
            throw new BusinessRuleViolationException("The new password must be different from the current one");
        }

        UserAccount userAccount = get(username);
        if (BCrypt.checkpw(currentPassword, userAccount.getPassword())) {
            if (!newPassword.matches(passwordRegex)) {
                throw new BusinessRuleViolationException("The new password must contain at least 5 characters");
            }

            String hashedPassword = PasswordHashingService.getHashedFromPlain(newPassword);
            userAccount.setPassword(hashedPassword);
            save(userAccount);
        }
        else {
            throw new UnauthorizedException("The provided currentPassword doesn't match the currently logged in " +
                    "user's password");
        }
    }
}
