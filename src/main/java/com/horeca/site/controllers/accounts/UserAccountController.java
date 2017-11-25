package com.horeca.site.controllers.accounts;

import com.horeca.site.models.accounts.*;
import com.horeca.site.security.models.UserAccount;
import com.horeca.site.security.services.UserAccountService;
import com.horeca.site.services.accounts.PasswordResetService;
import com.horeca.site.services.accounts.UserAccountPendingService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.Map;
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
    private UserAccountPendingService userAccountPendingService;

    @Autowired
    private PasswordResetService passwordResetService;

    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<UserAccountView> getViews() {
        return userAccountService.getViews();
    }

    @RequestMapping(value = "/users/current", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserAccountView getCurrentView(Authentication authentication) {
        return userAccountService.getFromAuthentication(authentication).toView();
    }

    @RequestMapping(value = "/users/current/password", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public void changePasswordOfCurrentAccount(Authentication authentication,
                                               @RequestBody PasswordChangeRequest request) {
        UserAccount userAccount = userAccountService.getFromAuthentication(authentication);
        userAccountService.verifyAndChangePassword(userAccount.getLogin(), request.currentPassword, request.newPassword);
    }

    @RequestMapping(value = "/users/current/profile-data", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> getProfileOfCurrentAccount(Authentication authentication) {
        UserAccount userAccount = userAccountService.getFromAuthentication(authentication);
        return userAccount.toView().getProfileData();
    }

    @RequestMapping(value = "/users/current/profile-data", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> updateProfileDataOfCurrentAccount(Authentication authentication,
                                                                 @RequestBody Map<String, String> profileData) {
        UserAccount userAccount = userAccountService.getFromAuthentication(authentication);
        return userAccountService.updateProfileData(userAccount.getLogin(), profileData);
    }

    @Transactional
    @RequestMapping(value = "/users", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMessage addPending(@RequestBody @Valid UserAccountPOST userAccountPOST) {
        AccountPending pending = userAccountPendingService.add(userAccountPOST);
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
