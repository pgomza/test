package com.horeca.site.services;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.exceptions.UnauthorizedException;
import com.horeca.site.models.accounts.UserAccountView;
import com.horeca.site.security.models.*;
import com.horeca.site.security.services.UserAccountService;
import com.horeca.site.security.services.UserAccountTempTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class AccountService {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private UserAccountService userAccountService;

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

    @PreAuthorize("hasRole('ROLE_SALESMAN')")
    public UserAccountTempTokenResponse getTempTokenForNewUserAccount(UserAccountTempTokenRequest request) {
        Set<String> roles = new HashSet<>(Arrays.asList(UserAccount.DEFAULT_ROLE));
        UserAccountTempToken tempToken = userAccountTempTokenService.generateTempToken(request.getHotelId(), roles);

        Integer expiresIn = userAccountTempTokenService.getSecondsUntilExpiration(tempToken);
        return new UserAccountTempTokenResponse(tempToken.getToken(), request.getHotelId(), expiresIn.intValue());
    }

    public UserAccountTempTokenResponse getInfoAboutUserAccountTempToken(String token) {
        UserAccountTempToken tempToken = userAccountTempTokenService.get(token);
        userAccountTempTokenService.ensureValidity(tempToken);
        int expiresIn = userAccountTempTokenService.getSecondsUntilExpiration(tempToken);
        return new UserAccountTempTokenResponse(tempToken.getToken(), tempToken.getHotel().getId(), expiresIn);
    }

    public UserAccountView addUserAccount(String token, UserAccountPOST userAccountPOST) {
        UserAccountTempToken tempToken;
        try {
            tempToken = userAccountTempTokenService.get(token);
            userAccountTempTokenService.ensureValidity(tempToken);
        }
        catch (ResourceNotFoundException ex) {
            throw new UnauthorizedException(ex); // in that case this exception makes more sense
        }

        Long allowedHotelId = tempToken.getHotel().getId();
        Long requestedHotelId = tempToken.getHotel().getId();

        if (!allowedHotelId.equals(requestedHotelId)) {
            throw new UnauthorizedException("The temp token you've sent doesn't authorize you to access the requested hotel");
        }

        String username = UserAccount.USERNAME_PREFIX + userAccountPOST.getLogin();
        // TODO refactor generating a hashed password into a separate method
        String salt = BCrypt.gensalt(12);
        String hashed_password = BCrypt.hashpw(userAccountPOST.getPassword(), salt);
        List<String> roles = new ArrayList<>(Arrays.asList(UserAccount.DEFAULT_ROLE));
        UserAccount userAccount = new UserAccount(username, hashed_password, allowedHotelId, roles);

        UserAccount savedUserAccount = userAccountService.save(userAccount);
        return savedUserAccount.toView();
    }
}
