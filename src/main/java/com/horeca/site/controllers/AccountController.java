package com.horeca.site.controllers;

import com.horeca.site.models.accounts.*;
import com.horeca.site.security.models.UserAccount;
import com.horeca.site.security.services.UserAccountService;
import com.horeca.site.services.accounts.AccountQueryService;
import com.horeca.site.services.accounts.PasswordResetService;
import com.horeca.site.services.accounts.UserAccountCreationService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.Set;

@Api(value = "hotels")
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private UserAccountCreationService userAccountCreationService;

    @Autowired
    private AccountQueryService accountQueryService;

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private UserAccountService userAccountService;

    @Value("${activation.redirectUrl}")
    private String activationRedirectUrl;

    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<UserAccountView> getUserAccountViews() {
        return userAccountService.getViews();
    }

    @RequestMapping(value = "/users/current", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserAccountView getCurrentUserAccount(Authentication authentication) {
        return accountQueryService.getCurrentUserAccount(authentication).toView();
    }

    @RequestMapping(value = "/users/current/password", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public void changePasswordOfCurrentUserAccount(Authentication authentication,
                                                   @RequestBody PasswordChangeRequest request) {
        UserAccount userAccount = accountQueryService.getCurrentUserAccount(authentication);
        userAccountService.verifyAndChangePassword(userAccount.getLogin(), request.currentPassword, request.newPassword);
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMessage addUserAccountPending(@RequestHeader(name = "Temp-Token", required = true) String token,
                                                 @RequestBody @Valid UserAccountPOST userAccountPOST) {
        UserAccountPending pending = userAccountCreationService.addUserAccountPending(token, userAccountPOST);
        try {
            userAccountCreationService.sendActivationEmail(pending);
        } catch (UnsupportedEncodingException | MessagingException e) {
            throw new RuntimeException("There was a problem while trying to send an email to " + pending.getEmail(), e);
        }

        return new ResponseMessage("The activation link has been sent to " + userAccountPOST.getEmail());
    }

    @RequestMapping(value = "/users/activation", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String activateUserAccount(@RequestParam(value = "secret") String secret) {
        String outcome = "Activation successful";
        try {
            boolean activationStatus = userAccountCreationService.activateUserAccount(secret);
            if (!activationStatus) {
                outcome = "Activation failed - invalid link";
            }
        } catch (RuntimeException ex) {
            outcome = "Activation failed";
        }

        return getRedirectPage(outcome, activationRedirectUrl);
    }

    @RequestMapping(value = "/users/tokens", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserAccountTempTokenResponse getTempTokenForNewUserAccount(@RequestBody UserAccountTempTokenRequest request) {
        return userAccountCreationService.getTempTokenForNewUserAccount(request);
    }

    @RequestMapping(value = "/users/tokens/{token}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserAccountTempTokenResponse getInfoAboutUserAccountTempToken(@PathVariable("token") String token) {
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

    private String getRedirectPage(String outcome, String redirectUrl) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Account activation</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <b>" + outcome + "</b>; redirecting to <a href=\"" + redirectUrl + "\">" + redirectUrl + "</a> in \n" +
                "    <span id=\"counter\">5</span><span id=\"counter-text\"> seconds</span>\n" +
                "\n" +
                "    <script>\n" +
                "        setInterval(function() {\n" +
                "            var counterSpan = document.querySelector(\"#counter\");\n" +
                "            var counterTextSpan = document.querySelector(\"#counter-text\");\n" +
                "\n" +
                "            var count = counterSpan.textContent * 1 - 1;\n" +
                "\n" +
                "            if (count === 1) {\n" +
                "                counterTextSpan.textContent = \" second\"\n" +
                "            }\n" +
                "\n" +
                "            if (count > 0) {\n" +
                "                counterSpan.textContent = count;\n" +
                "            }\n" +
                "            else {\n" +
                "                window.location.href=\"" + redirectUrl + "\";\n" +
                "            }\n" +
                "        }, 1000);\n" +
                "    </script>\n" +
                "</body>\n" +
                "</html>";
    }

    public static class ResponseMessage {
        private String message;

        public ResponseMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
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
