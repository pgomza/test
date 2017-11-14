package com.horeca.site.controllers.accounts;

import com.horeca.site.models.accounts.SalesmanAccountPOST;
import com.horeca.site.models.accounts.SalesmanAccountView;
import com.horeca.site.security.models.SalesmanAccount;
import com.horeca.site.security.services.SalesmanAccountService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api(value = "hotels")
@RestController
@RequestMapping("/api/accounts")
public class SalesmanAccountController {

    @Autowired
    private SalesmanAccountService service;

    @RequestMapping(value = "/salesmen", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SalesmanAccountView> getAll() {
        return service.getAll().stream().map(SalesmanAccount::toView).collect(Collectors.toList());
    }

    @RequestMapping(value = "/salesmen", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public SalesmanAccountView create(@RequestBody @Valid SalesmanAccountPOST accountPOST) {
        return service.create(accountPOST.getLogin(), accountPOST.getPassword()).toView();
    }

    @RequestMapping(value = "/salesmen/current", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public SalesmanAccountView getCurrentView(Authentication authentication) {
        return authenticationToSalesmanAccount(authentication).toView();
    }

    @RequestMapping(value = "/salesmen/{login}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public SalesmanAccountView get(@PathVariable("login") String login) {
        return service.get(login).toView();
    }

    @RequestMapping(value = "/salesmen/{login}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable("login") String login) {
        service.delete(login);
    }

    private SalesmanAccount authenticationToSalesmanAccount(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof SalesmanAccount) {
            return (SalesmanAccount) principal;
        }
        else
            throw new AccessDeniedException("Access denied");
    }
}
