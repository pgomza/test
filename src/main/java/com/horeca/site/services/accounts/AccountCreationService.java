package com.horeca.site.services.accounts;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.exceptions.UnauthorizedException;
import com.horeca.site.models.accounts.*;
import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.security.models.UserAccount;
import com.horeca.site.security.services.PasswordHashingService;
import com.horeca.site.security.services.UserAccountService;
import com.horeca.site.services.EmailSenderService;
import com.horeca.site.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
@Transactional
public class AccountCreationService {

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private UserAccountPendingService userAccountPendingService;

    @Autowired
    private UserAccountTempTokenService userAccountTempTokenService;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private EmailSenderService emailSenderService;

    @Value("${activation.url}")
    private String activationUrl;

    @PreAuthorize("hasRole('ROLE_SALESMAN')")
    public UserAccountTempTokenResponse getTempTokenForNewUserAccount(UserAccountTempTokenRequest request) {
        Set<String> roles = new HashSet<>(Collections.singletonList(UserAccount.DEFAULT_ROLE));
        UserAccountTempToken tempToken = userAccountTempTokenService.generateTempToken(request.getHotelId(), roles);

        Integer expiresIn = userAccountTempTokenService.getSecondsUntilExpiration(tempToken);
        return new UserAccountTempTokenResponse(tempToken.getToken(), request.getHotelId(), expiresIn);
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

        // to prevent people from creating several user accounts with the same temp token, invalidate it
        userAccountTempTokenService.invalidate(tempToken);

        return saved;
    }

    public String activateUserAccountAndGetRedirectUrl(String secret) {
        activateUserAccount(secret);
        UserAccountPending userAccountPending = userAccountPendingService.getBySecret(secret);
        String redirectUrl = userAccountPending.getRedirectUrl();
        userAccountPendingService.delete(userAccountPending.getEmail());
        return redirectUrl;
    }

    private void activateUserAccount(String secret) {
        UserAccountPending userAccountPending = userAccountPendingService.getBySecret(secret);
        if (userAccountPending == null) {
            throw new BusinessRuleViolationException("Invalid secret");
        }

        List<String> roles = new ArrayList<>(Collections.singletonList(UserAccount.DEFAULT_ROLE));
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

    public void sendActivationEmail(UserAccountPending account) throws MessagingException, UnsupportedEncodingException {
        String link = activationUrl + account.getSecret();
        String messageBody =
                "<div>" +
                        "Hi," +
                        "<br /><br />" +
                        "to activate your account, please click the link below:<br/>" + link +
                        "<br/><br />" +
                        "Regards," +
                        "<br/>" +
                        "The Throdi Team" +
                        "</div>";

        emailSenderService.sendStandard("Account activation", messageBody, account.getEmail());
    }
}
