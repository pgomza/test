package com.horeca.site.controllers;

import com.horeca.site.models.accounts.UserAccountView;
import com.horeca.site.security.models.*;
import com.horeca.site.security.services.UserAccountPendingService;
import com.horeca.site.security.services.UserAccountService;
import com.horeca.site.services.AccountService;
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
    private AccountService service;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private UserAccountPendingService userAccountPendingService;

    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<UserAccountView> getUserAccountViews() {
        return userAccountService.getViews();
    }

    @RequestMapping(value = "/users/current", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserAccountView getCurrentUserAccount(Authentication authentication) {
        return service.getCurrentUserAccount(authentication).toView();
    }

    @RequestMapping(value = "/users/current/password", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public void changePasswordOfCurrentUserAccount(Authentication authentication,
                                                   @RequestBody PasswordChangeRequest request) {
        UserAccount userAccount = service.getCurrentUserAccount(authentication);
        userAccountService.changePassword(userAccount.getUsername(), request.currentPassword, request.newPassword);
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMessage addUserAccountPending(@RequestHeader(name = "Temp-Token", required = true) String token,
                                                 @RequestBody @Valid UserAccountPOST userAccountPOST) {
        service.addUserAccountPending(token, userAccountPOST);
        return new ResponseMessage("The activation link has been sent to " + userAccountPOST.getEmail());
    }

    @RequestMapping(value = "/users/activation", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> activateUserAccount(@RequestParam(value = "secret", required = true) String secret) {
        service.activateUserAccount(secret);
        UserAccountPending userAccountPending = userAccountPendingService.getBySecret(secret);
        String redirectUrl = userAccountPending.getRedirectUrl();
        userAccountPendingService.delete(userAccountPending.getEmail());

        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("Location", redirectUrl);
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @RequestMapping(value = "/users/tokens", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserAccountTempTokenResponse getTempTokenForNewUserAccount(@RequestBody UserAccountTempTokenRequest request) {
        return service.getTempTokenForNewUserAccount(request);
    }

    @RequestMapping(value = "/users/tokens/{token}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserAccountTempTokenResponse getInfoAboutUserAccountTempToken(@PathVariable("token") String token) {
        return service.getInfoAboutUserAccountTempToken(token);
    }

    public static class ResponseMessage {
        private String message;

        public ResponseMessage(String message) {
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
