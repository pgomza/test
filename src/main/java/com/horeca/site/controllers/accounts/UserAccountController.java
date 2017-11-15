package com.horeca.site.controllers.accounts;

import com.horeca.site.models.accounts.*;
import com.horeca.site.security.models.UserAccount;
import com.horeca.site.security.services.UserAccountService;
import com.horeca.site.services.accounts.PasswordResetService;
import com.horeca.site.services.accounts.UserAccountCreationService;
import com.horeca.site.services.accounts.UserAccountPendingService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.Set;

import static com.horeca.site.services.accounts.AccountPendingService.ResponseMessage;
import static com.horeca.site.services.accounts.AccountPendingService.prepareResponseMessage;

@Api(value = "hotels")
@RestController
@RequestMapping("/api/accounts")
public class UserAccountController {

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private UserAccountCreationService userAccountCreationService;

    @Autowired
    private UserAccountPendingService userAccountPendingService;

    @Autowired
    private PasswordResetService passwordResetService;

    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<UserAccountView> getViews() {
        return userAccountService.getViews();
    }

    @RequestMapping(value = "/users/current", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserAccountView getCurrentView(Authentication authentication) {
        return authenticationToUserAccount(authentication).toView();
    }

    @RequestMapping(value = "/users/current/password", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public void changePasswordOfCurrentAccount(Authentication authentication,
                                               @RequestBody PasswordChangeRequest request) {
        UserAccount userAccount = authenticationToUserAccount(authentication);
        userAccountService.verifyAndChangePassword(userAccount.getLogin(), request.currentPassword, request.newPassword);
    }

    @Transactional
    @RequestMapping(value = "/users", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMessage addPending(@RequestHeader(name = "Temp-Token", required = true) String token,
                                      @RequestBody @Valid UserAccountPOST userAccountPOST) {
        AccountPending pending = userAccountPendingService.verifyAndAdd(token, userAccountPOST);
        try {
            userAccountPendingService.sendActivationEmail(pending);
        } catch (UnsupportedEncodingException | MessagingException e) {
            throw new RuntimeException("There was a problem while trying to send an email to " + pending.getEmail(), e);
        }

        return prepareResponseMessage(pending.getEmail());
    }

    @RequestMapping(value = "/users/activation", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String activate(@RequestParam(value = "secret") String secret) {
        String outcome = "Activation successful";
        try {
            userAccountPendingService.activate(secret);
        } catch (RuntimeException ex) {
            outcome = "Activation failed";
        }

        return userAccountPendingService.prepareRedirectPage(outcome);
    }

    @RequestMapping(value = "/users/tokens", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserAccountTempTokenResponse getNewTempToken(@RequestBody UserAccountTempTokenRequest request) {
        return userAccountCreationService.getTempTokenForNewUserAccount(request);
    }

    @RequestMapping(value = "/users/tokens/{token}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserAccountTempTokenResponse getInfoAboutToken(@PathVariable("token") String token) {
        return userAccountCreationService.getInfoAboutUserAccountTempToken(token);
    }

    @RequestMapping(value = "/users/reset-request", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void handleResetRequest(@Valid @RequestBody PasswordResetRequest request) {
        PasswordResetPending pending = passwordResetService.handleRequest(request);
        try {
            passwordResetService.sendEmail(request.getLogin(), request.getRedirectUrl(), pending.getSecret());
        } catch (UnsupportedEncodingException | MessagingException e) {
            throw new RuntimeException("There was a problem while trying to send an email to " + request.getLogin(), e);
        }
    }

    @RequestMapping(value = "/users/reset-confirmation", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void confirmPasswordReset(@Valid @RequestBody PasswordResetConfirmation confirmation) {
        passwordResetService.handleConfirmation(confirmation);
    }

    private UserAccount authenticationToUserAccount(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserAccount) {
            return (UserAccount) principal;
        }
        else
            throw new AccessDeniedException("Access denied");
    }

    public static class PasswordChangeRequest {
        private String currentPassword;
        private String newPassword;

        public String getCurrentPassword() {
            return currentPassword;
        }

        public void setCurrentPassword(String currentPassword) {
            this.currentPassword = currentPassword;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }
}
