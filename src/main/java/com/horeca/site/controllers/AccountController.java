package com.horeca.site.controllers;

import com.horeca.site.models.accounts.UserAccountView;
import com.horeca.site.security.models.UserAccount;
import com.horeca.site.security.models.UserAccountPOST;
import com.horeca.site.security.models.UserAccountTempTokenRequest;
import com.horeca.site.security.models.UserAccountTempTokenResponse;
import com.horeca.site.security.services.UserAccountService;
import com.horeca.site.services.AccountCreationService;
import com.horeca.site.services.AccountQueryService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@Api(value = "hotels")
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountCreationService accountCreationService;

    @Autowired
    private AccountQueryService accountQueryService;

    @Autowired
    private UserAccountService userAccountService;

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
        userAccountService.changePassword(userAccount.getUsername(), request.currentPassword, request.newPassword);
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMessage addUserAccountPending(@RequestHeader(name = "Temp-Token", required = true) String token,
                                                 @RequestBody @Valid UserAccountPOST userAccountPOST) {
        accountCreationService.addUserAccountPending(token, userAccountPOST);
        return new ResponseMessage("The activation link has been sent to " + userAccountPOST.getEmail());
    }

    @RequestMapping(value = "/users/activation", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> activateUserAccount(@RequestParam(value = "secret", required = true) String secret) {
        String redirectUrl = accountCreationService.activateUserAccountAndGetRedirectUrl(secret);
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("Location", redirectUrl);
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @RequestMapping(value = "/users/tokens", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserAccountTempTokenResponse getTempTokenForNewUserAccount(@RequestBody UserAccountTempTokenRequest request) {
        return accountCreationService.getTempTokenForNewUserAccount(request);
    }

    @RequestMapping(value = "/users/tokens/{token}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserAccountTempTokenResponse getInfoAboutUserAccountTempToken(@PathVariable("token") String token) {
        return accountCreationService.getInfoAboutUserAccountTempToken(token);
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
