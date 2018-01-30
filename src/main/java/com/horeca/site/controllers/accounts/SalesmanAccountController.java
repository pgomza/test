package com.horeca.site.controllers.accounts;

import com.horeca.site.models.accounts.AccountPOST;
import com.horeca.site.models.accounts.AccountPending;
import com.horeca.site.models.accounts.SalesmanAccountPending;
import com.horeca.site.models.accounts.SalesmanAccountView;
import com.horeca.site.security.services.SalesmanAccountService;
import com.horeca.site.services.accounts.AccountPendingService;
import com.horeca.site.services.accounts.SalesmanAccountPendingService;
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
import java.util.stream.Collectors;

@Api(value = "hotels")
@RestController
@RequestMapping("/api/accounts")
public class SalesmanAccountController {

    @Autowired
    private SalesmanAccountService accountService;

    @Autowired
    private SalesmanAccountPendingService pendingService;

    @Transactional
    @RequestMapping(value = "/salesmen", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<SalesmanAccountView> getViews(Pageable pageable) {
        List<SalesmanAccountView> allActivated = accountService.getAllViews();
        List<SalesmanAccountView> allPending = pendingService.getAll().stream()
                .map(SalesmanAccountPending::toView)
                .collect(Collectors.toList());
        allActivated.addAll(allPending);
        return PageableUtils.extractPage(allActivated, pageable);
    }

    @RequestMapping(value = "/salesmen", params = { "activated" }, method = RequestMethod.GET, produces = MediaType
            .APPLICATION_JSON_VALUE)
    public Page<SalesmanAccountView> getViewsByActivated(Pageable pageable, @RequestParam(value = "activated") Boolean activated) {
        if (activated != null) {
            if (activated) {
                return accountService.getAllViews(pageable);
            }
            else {
                List<SalesmanAccountView> allPending = pendingService.getAll().stream()
                        .map(SalesmanAccountPending::toView)
                        .collect(Collectors.toList());
                return PageableUtils.extractPage(allPending, pageable);
            }
        }
        else {
            return PageableUtils.emptyPage();
        }
    }

    @Transactional
    @RequestMapping(value = "/salesmen/{login:.+}", method = RequestMethod.GET, produces = MediaType
            .APPLICATION_JSON_VALUE)
    public SalesmanAccountView get(@PathVariable("login") String login) {
        if (accountService.exists(login)) {
            return accountService.get(login).toView();
        }
        else {
            return pendingService.get(login).toView();
        }
    }

    @RequestMapping(value = "/salesmen/{login:.+}", method = RequestMethod.DELETE, produces = MediaType
            .APPLICATION_JSON_VALUE)
    public void delete(@PathVariable("login") String login) {
        accountService.delete(login);
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
}
