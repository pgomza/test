package com.horeca.site.controllers.accounts;

import com.horeca.site.models.accounts.*;
import com.horeca.site.security.services.UserAccountService;
import com.horeca.site.services.accounts.PasswordResetService;
import com.horeca.site.services.accounts.UserAccountPendingService;
import com.horeca.utils.PageableUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @Transactional
    @RequestMapping(value = "/users", params = { "activated" }, method = RequestMethod.GET, produces = MediaType
            .APPLICATION_JSON_VALUE)
    public Page<UserAccountView> getViews(Pageable pageable, @RequestParam(value = "activated", required = false)
                                          Boolean activated) {
        if (activated != null && activated) {
            return userAccountService.getAllViews(pageable);
        }
        else {
            List<UserAccountView> allActivated = userAccountService.getAllViews();
            List<UserAccountView> allPending = userAccountPendingService.getAll()
                    .stream().map(UserAccountPending::toView).collect(Collectors.toList());
            allActivated.addAll(allPending);
            return PageableUtils.extractPage(allActivated, pageable);
        }
    }

    @Transactional
    @RequestMapping(value = "/users", params = { "activated", "hotel-id" }, method = RequestMethod.GET, produces =
            MediaType.APPLICATION_JSON_VALUE)
    public Page<UserAccountView> getViews(Pageable pageable, @RequestParam(value = "hotel-id") Long hotelId,
                                          @RequestParam(value = "activated", required = false) Boolean activated) {
        if (hotelId != null) {
            if (activated != null && activated) {
                return userAccountService.getAllViews(hotelId, pageable);
            }
            else {
                List<UserAccountView> allActivated = userAccountService.getAllViews(hotelId);
                List<UserAccountView> allPending = userAccountPendingService.getAll()
                        .stream()
                        .filter(a -> Objects.equals(a.getHotelId(), hotelId))
                        .map(UserAccountPending::toView).collect(Collectors.toList());
                allActivated.addAll(allPending);
                return PageableUtils.extractPage(allActivated, pageable);
            }
        }
        else {
            return new PageImpl<>(Collections.emptyList());
        }
    }

    @Transactional
    @RequestMapping(value = "/users/{login:.+}", method = RequestMethod.GET, produces = MediaType
            .APPLICATION_JSON_VALUE)
    public UserAccountView get(@PathVariable("login") String login) {
        if (userAccountService.exists(login)) {
            return userAccountService.get(login).toView();
        }
        else {
            return userAccountPendingService.get(login).toView();
        }
    }

    @RequestMapping(value = "/users/{login:.+}", method = RequestMethod.DELETE, produces = MediaType
            .APPLICATION_JSON_VALUE)
    public void delete(@PathVariable("login") String login) {
        userAccountService.delete(login);
    }

    @RequestMapping(value = "/users/{login:.+}/password", method = RequestMethod.PUT, produces = MediaType
            .APPLICATION_JSON_VALUE)
    public void changePasswordOfCurrentAccount(@PathVariable("login") String login,
                                               @RequestBody PasswordChangeRequest request) {
        userAccountService.verifyAndChangePassword(login, request.currentPassword, request.newPassword);
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
