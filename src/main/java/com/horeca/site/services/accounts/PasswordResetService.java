package com.horeca.site.services.accounts;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.accounts.PasswordResetConfirmation;
import com.horeca.site.models.accounts.PasswordResetPending;
import com.horeca.site.models.accounts.PasswordResetRequest;
import com.horeca.site.security.models.UserAccount;
import com.horeca.site.security.services.UserAccountService;
import com.horeca.site.services.EmailSenderService;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.UUID;

import static com.horeca.site.security.OAuth2AuthorizationServerConfig.PANEL_CLIENT_ID;

@Service
@Transactional
public class PasswordResetService {

    private static final String INVALID_SECRET_MESSAGE = "The provided secret is invalid";

    @Autowired
    private PasswordResetPendingService passwordResetPendingService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private TokenStore tokenStore;

    public void handlePasswordResetRequest(PasswordResetRequest request) {
        String login = request.getLogin();
        // not sure if the validator is thread-safe; otherwise it would have been declared as a static variable
        EmailValidator emailValidator = new EmailValidator();
        if (!emailValidator.isValid(login, null)) {
            throw new RuntimeException("Either the login is not an email address or there is no email address " +
                    "associated with the login");
        }

        UserAccount userAccount;
        try {
            userAccount = userAccountService.get(UserAccount.USERNAME_PREFIX + login);
            if (!userAccount.isEnabled()) {
                throw new ResourceNotFoundException();
            }
        } catch (ResourceNotFoundException ex) {
            // the account either has not been found or is disabled, but the user shouldn't know the specifics
            throw new ResourceNotFoundException("Could not find a user that matches the login");
        }

        String secret = generateSecret();
        Long expirationTimestamp = Instant.now().plus(Duration.standardHours(1L)).getMillis();

        PasswordResetPending pending = new PasswordResetPending(userAccount, secret, expirationTimestamp);
        passwordResetPendingService.save(pending);

        String finalRedirectUrl = request.getRedirectUrl() + "?secret=" + secret;
        try {
            sendPasswordResetEmail(login, finalRedirectUrl);
        } catch (UnsupportedEncodingException | MessagingException e) {
            throw new RuntimeException("There was a problem while trying to send an email to " + login, e);
        }
    }

    public void confirmPasswordReset(PasswordResetConfirmation confirmation) {
        PasswordResetPending pending;
        try {
            String secret = confirmation.getSecret();
            pending = passwordResetPendingService.getBySecret(secret);
        } catch (ResourceNotFoundException ex) {
            throw new BusinessRuleViolationException(INVALID_SECRET_MESSAGE);
        }

        if (passwordResetPendingService.isValid(pending)) {
            String newPassword = confirmation.getNewPassword();
            userAccountService.changePassword(pending.getUsername(), newPassword);
            passwordResetPendingService.delete(pending);

            // delete all the OAuth2 tokens associated with this account
            Collection<OAuth2AccessToken> tokens = tokenStore.
                    findTokensByClientIdAndUserName(PANEL_CLIENT_ID, pending.getUsername());
            tokens.forEach(tokenStore::removeAccessToken);
        }
        else {
            throw new BusinessRuleViolationException(INVALID_SECRET_MESSAGE);
        }
    }

    private String generateSecret() {
        return UUID.randomUUID().toString();
    }

    private void sendPasswordResetEmail(String emailAddress, String redirectUrl) throws UnsupportedEncodingException,
            MessagingException {
        String content =
                "<div>" +
                        "Hi," +
                        "<br/><br/>" +
                        "to proceed with resetting your password, please click the link below:<br/>" + redirectUrl +
                        "<br/><br/>" +
                        "Regards," +
                        "<br/>" +
                        "The Throdi Team" +
                        "</div>";

        emailSenderService.sendStandard("Password reset", content, emailAddress,
                "throdi.auto@gmail.com", "Throdi");
    }
}
