package com.horeca.site.services.accounts;
import com.horeca.site.security.models.UserAccount;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountQueryService {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserAccount getCurrentUserAccount(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserAccount) {
            UserAccount userAccount = (UserAccount) principal;
            return userAccount;
        }
        else
            throw new AccessDeniedException("Access denied");
    }
}
