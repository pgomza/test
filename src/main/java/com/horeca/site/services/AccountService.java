package com.horeca.site.services;

import com.horeca.site.models.accounts.UserAccountView;
import com.horeca.site.security.UserAccount;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountService {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserAccountView getCurrentUserAccount(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserAccount) { // should always be true
            UserAccount userAccount = (UserAccount) principal;
            return userAccount.toView();
        }
        else
            throw new AccessDeniedException("Access denied");
    }
}
