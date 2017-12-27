package com.horeca.site.controllers.accounts;

import com.horeca.site.models.accounts.AccountPOST;
import com.horeca.site.models.accounts.AccountPending;
import com.horeca.site.models.accounts.SalesmanAccountView;
import com.horeca.site.security.models.SalesmanAccount;
import com.horeca.site.security.services.SalesmanAccountService;
import com.horeca.site.services.accounts.AccountPendingService;
import com.horeca.site.services.accounts.SalesmanAccountPendingService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Api(value = "hotels")
@RestController
@RequestMapping("/api/accounts")
public class SalesmanAccountController {

    @Autowired
    private SalesmanAccountService accountService;

    @Autowired
    private SalesmanAccountPendingService pendingService;

    @RequestMapping(value = "/salesmen", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SalesmanAccountView> getAll() {
        return accountService.getAll().stream().map(SalesmanAccount::toView).collect(Collectors.toList());
    }

    @Transactional
    @RequestMapping(value = "/salesmen", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public AccountPendingService.ResponseMessage addPending(@RequestBody @Valid AccountPOST accountPOST) {
        AccountPending pending = pendingService.add(accountPOST);
        try {
            pendingService.sendActivationEmail(pending);
        } catch (UnsupportedEncodingException | MessagingException e) {
            throw new RuntimeException("There was a problem while trying to send an email to " + pending.getEmail(), e);
        }
        return AccountPendingService.prepareResponseMessage(pending.getEmail());
    }

    @RequestMapping(value = "/salesmen/activation", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String activate(@RequestParam(value = "secret") String secret) {
        String outcome = "Activation successful";
        try {
            pendingService.activate(secret);
        } catch (RuntimeException ex) {
            outcome = "Activation failed";
        }

        return pendingService.prepareRedirectPage(outcome);
    }

    @RequestMapping(value = "/salesmen/current", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public SalesmanAccountView getCurrentView(Authentication authentication) {
        return accountService.getFromAuthentication(authentication).toView();
    }

    @RequestMapping(value = "/salesmen/current/profile-data", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> getProfileOfCurrentAccount(Authentication authentication) {
        SalesmanAccount salesmanAccount = accountService.getFromAuthentication(authentication);
        return salesmanAccount.getProfileData();
    }

    @RequestMapping(value = "/salesmen/current/profile-data", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> updateProfileDataOfCurrentAccount(Authentication authentication,
                                                                 @RequestBody Map<String, String> profileData) {
        SalesmanAccount salesmanAccount = accountService.getFromAuthentication(authentication);
        return accountService.updateProfileData(salesmanAccount.getLogin(), profileData);
    }

    @RequestMapping(value = "/salesmen/{login}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public SalesmanAccountView get(@PathVariable("login") String login) {
        return accountService.get(login).toView();
    }

    @RequestMapping(value = "/salesmen/{login}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable("login") String login) {
        accountService.delete(login);
    }
}
