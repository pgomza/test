package com.horeca.site.services;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.exceptions.UnauthorizedException;
import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.security.models.*;
import com.horeca.site.security.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.*;

@Service
@Transactional
public class AccountService {

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private UserAccountPendingService userAccountPendingService;

    @Autowired
    private UserAccountTempTokenService userAccountTempTokenService;

    @Autowired
    private UserAccountEmailService userAccountEmailService;

    @Autowired
    private HotelService hotelService;

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

    public UserAccountPending addUserAccountPending(String token, UserAccountPOST userAccountPOST) {
        UserAccountTempToken tempToken;
        try {
            tempToken = userAccountTempTokenService.get(token);
            userAccountTempTokenService.ensureValidity(tempToken);
        }
        catch (ResourceNotFoundException ex) {
            throw new UnauthorizedException(ex); // in that case this exception makes more sense
        }

        String username = UserAccount.USERNAME_PREFIX + userAccountPOST.getEmail();
        if (userAccountService.exists(username)) {
            throw new BusinessRuleViolationException("A user with such an email already exists");
        }

        String hashedPassword = PasswordHashingService.getHashedFromPlain(userAccountPOST.getPassword());
        String secret = userAccountTempTokenService.generateRandomString();
        String redirectUrl = userAccountPOST.getRedirectUrl();
        if (redirectUrl.endsWith("/"))
            redirectUrl = redirectUrl.substring(0, redirectUrl.length() - 1);

        UserAccountPending userAccountPending =
                new UserAccountPending(userAccountPOST.getEmail(), hashedPassword, tempToken.getHotel().getId(), secret, redirectUrl);
        UserAccountPending saved = userAccountPendingService.save(userAccountPending);

        try {
            userAccountEmailService.sendActivation(userAccountPending);
        } catch (MessagingException e) {
            throw new RuntimeException("There was a problem while trying to send the activation email", e);
        }

        // to prevent people from creating several user accounts with the same temp token, invalidate it
        userAccountTempTokenService.invalidate(tempToken);

        return saved;
    }

    public void activateUserAccount(String secret) {
        UserAccountPending userAccountPending = userAccountPendingService.getBySecret(secret);
        if (userAccountPending == null) {
            throw new BusinessRuleViolationException("Invalid secret");
        }

        List<String> roles = new ArrayList<>(Arrays.asList(UserAccount.DEFAULT_ROLE));
        UserAccount userAccount =
                new UserAccount(userAccountPending.getEmail(), userAccountPending.getPassword(),
                        userAccountPending.getHotelId(), roles);
        userAccountService.save(userAccount);

        // this may be the first user for this hotel
        // make sure that the hotel contains enough information
        hotelService.ensureEnoughInfoAboutHotel(userAccount.getHotelId());

        Hotel hotel = hotelService.get(userAccount.getHotelId());
        hotel.setIsThrodiPartner(true);
        hotelService.update(hotel.getId(), hotel);
    }
}
