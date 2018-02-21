package com.horeca.site.controllers.accounts;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.models.accounts.*;
import com.horeca.site.security.services.UserAccountService;
import com.horeca.site.services.accounts.PasswordResetService;
import com.horeca.site.services.accounts.UserAccountPendingService;
import com.horeca.utils.PageableUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
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
    private UserAccountService accountService;

    @Autowired
    private UserAccountPendingService pendingService;

    @Autowired
    private PasswordResetService passwordResetService;

    @Transactional
    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<UserAccountView> getViews(Pageable pageable) {
        List<UserAccountView> allActivated = accountService.getAllViews();
        List<UserAccountView> allPending = pendingService.getAll().stream()
                .map(UserAccountPending::toView)
                .collect(Collectors.toList());
        allActivated.addAll(allPending);
        return PageableUtils.extractPage(allActivated, pageable);
    }

    @RequestMapping(value = "/users", params = { "activated" }, method = RequestMethod.GET, produces = MediaType
            .APPLICATION_JSON_VALUE)
    public Page<UserAccountView> getViewsByActivated(Pageable pageable, @RequestParam(value = "activated") Boolean activated) {
        if (activated != null) {
            if (activated) {
                return accountService.getAllViews(pageable);
            }
            else {
                List<UserAccountView> allPending = pendingService.getAll().stream()
                        .map(UserAccountPending::toView)
                        .collect(Collectors.toList());
                return PageableUtils.extractPage(allPending, pageable);
            }
        }
        else {
            return PageableUtils.emptyPage();
        }
    }

    @Transactional
    @RequestMapping(value = "/users", params = { "hotel-id" }, method = RequestMethod.GET, produces =
            MediaType.APPLICATION_JSON_VALUE)
    public Page<UserAccountView> getViewsByHotelId(Pageable pageable, @RequestParam(value = "hotel-id") Long hotelId) {
        if (hotelId != null) {
            List<UserAccountView> allActivated = accountService.getAllViews(hotelId);
            List<UserAccountView> allPending = pendingService.getAll().stream()
                    .filter(a -> Objects.equals(a.getHotelId(), hotelId))
                    .map(UserAccountPending::toView)
                    .collect(Collectors.toList());
            allActivated.addAll(allPending);
            return PageableUtils.extractPage(allActivated, pageable);
        }
        else {
            return PageableUtils.emptyPage();
        }
    }

    @RequestMapping(value = "/users", params = { "activated", "hotel-id" }, method = RequestMethod.GET, produces =
            MediaType.APPLICATION_JSON_VALUE)
    public Page<UserAccountView> getViewsByActivatedAndHotelId(Pageable pageable, @RequestParam(value = "activated") Boolean
            activated, @RequestParam(value = "hotel-id") Long hotelId) {
        if (activated != null && hotelId != null) {
            if (activated) {
                return accountService.getAllViews(hotelId, pageable);
            }
            else {
                List<UserAccountView> allPending = pendingService.getAll().stream()
                        .filter(a -> Objects.equals(a.getHotelId(), hotelId))
                        .map(UserAccountPending::toView)
                        .collect(Collectors.toList());
                return PageableUtils.extractPage(allPending, pageable);
            }
        }
        else {
            return PageableUtils.emptyPage();
        }
    }

    @Transactional
    @RequestMapping(value = "/users/{login:.+}", method = RequestMethod.GET, produces = MediaType
            .APPLICATION_JSON_VALUE)
    public UserAccountView get(@PathVariable("login") String login) {
        if (accountService.exists(login)) {
            return accountService.get(login).toView();
        }
        else {
            return pendingService.get(login).toView();
        }
    }

    @RequestMapping(value = "/users/{login:.+}", method = RequestMethod.DELETE, produces = MediaType
            .APPLICATION_JSON_VALUE)
    public void delete(@PathVariable("login") String login) {
        accountService.delete(login);
    }

    @RequestMapping(value = "/users/{login:.+}/password", method = RequestMethod.PUT, produces = MediaType
            .APPLICATION_JSON_VALUE)
    public void changePasswordOfCurrentAccount(@PathVariable("login") String login,
                                               @RequestBody PasswordChangeRequest request) {
        accountService.verifyAndChangePassword(login, request.currentPassword, request.newPassword);
    }

    @Transactional
    @RequestMapping(value = "/users", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMessage addPending(@RequestBody @Valid UserAccountPOST userAccountPOST) {
        AccountPending pending = pendingService.add(userAccountPOST);
        try {
            pendingService.sendActivationEmail(pending);
        } catch (UnsupportedEncodingException | MessagingException e) {
            throw new RuntimeException("There was a problem while trying to send an email to " + pending.getEmail(), e);
        }

        return prepareResponseMessage(pending.getEmail());
    }

    @Transactional
    @RequestMapping(value = "/users-new-hotel", method = RequestMethod.POST, produces = MediaType
            .APPLICATION_JSON_VALUE)
    public ResponseMessage addPendingWithNewHotel(@RequestBody @Valid UserAccountNewHotelPOST userAccountNewHotelPOST) {
        AccountPending pending = pendingService.add(userAccountNewHotelPOST);
        try {
            pendingService.sendActivationEmail(pending);
        } catch (UnsupportedEncodingException | MessagingException e) {
            throw new RuntimeException("There was a problem while trying to send an email to " + pending.getEmail(), e);
        }

        return prepareResponseMessage(pending.getEmail());
    }

    @RequestMapping(value = "/users/activation", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String activateAsAnonymous(@RequestParam(value = "secret") String secret) {
        String outcome = "Activation successful";
        try {
            pendingService.activateAsAnonymous(secret);
        } catch (RuntimeException ex) {
            outcome = "Activation failed";
        }

        return pendingService.prepareRedirectPage(outcome);
    }

    @Transactional
    @RequestMapping(value = "/users/activation/{login:.+}", method = RequestMethod.POST, produces = MediaType
            .APPLICATION_JSON_VALUE)
    public void activate(@PathVariable("login") String login) {
        if (!accountService.exists(login)) {
            try {
                pendingService.activate(login);
            } catch (RuntimeException ex) {
                throw new BusinessRuleViolationException("Activation failed: " + ex.getMessage());
            }
        }
        else {
            throw new BusinessRuleViolationException("Activation failed: this account has already been activated");
        }
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
