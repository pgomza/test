package com.horeca.site.services;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.accounts.UserAccountView;
import com.horeca.site.security.models.UserAccount;
import com.horeca.site.security.models.UserAccountTempToken;
import com.horeca.site.security.models.UserAccountTempTokenRequest;
import com.horeca.site.security.models.UserAccountTempTokenResponse;
import com.horeca.site.security.services.UserAccountTempTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class AccountService {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private UserAccountTempTokenService userAccountTempTokenService;

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

    public UserAccountTempTokenResponse getTempTokenForNewUserAccount(UserAccountTempTokenRequest request) {
        Set<String> roles = new HashSet<>(Arrays.asList(UserAccount.DEFAULT_ROLE));
        UserAccountTempToken tempToken = userAccountTempTokenService.generateTempToken(request.getHotelId(), roles);

        Integer expiresIn = userAccountTempTokenService.getSecondsUntilExpiration(tempToken);
        return new UserAccountTempTokenResponse(tempToken.getToken(), request.getHotelId(), expiresIn.intValue());
    }

    public UserAccountTempTokenResponse getInfoAboutUserAccountTempToken(String token) {
        UserAccountTempToken tempToken = userAccountTempTokenService.get(token);
        boolean isValid = userAccountTempTokenService.isValid(tempToken);

        if (!isValid) {
            throw new ResourceNotFoundException("The requested token has been invalidated or has never existed");
        }

        int expiresIn = userAccountTempTokenService.getSecondsUntilExpiration(tempToken);
        return new UserAccountTempTokenResponse(tempToken.getToken(), tempToken.getHotel().getId(), expiresIn);
    }
}
