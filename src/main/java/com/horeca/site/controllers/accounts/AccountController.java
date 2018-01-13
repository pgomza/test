package com.horeca.site.controllers.accounts;

import com.horeca.site.models.accounts.AccountView;
import com.horeca.site.security.models.RootAccount;
import com.horeca.site.security.models.SalesmanAccount;
import com.horeca.site.security.models.UserAccount;
import com.horeca.site.security.services.RootAccountService;
import com.horeca.site.security.services.SalesmanAccountService;
import com.horeca.site.security.services.UserAccountService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "hotels")
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private RootAccountService rootAccountService;

    @Autowired
    private SalesmanAccountService salesmanAccountService;

    @Autowired
    private UserAccountService userAccountService;

    @RequestMapping(value = "/current", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public AccountView getCurrentView(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof RootAccount) {
            return rootAccountService.getFromAuthentication(authentication, RootAccount.class).toView();
        }
        else if (principal instanceof SalesmanAccount) {
            return salesmanAccountService.getFromAuthentication(authentication, SalesmanAccount.class).toView();
        }
        else if (principal instanceof UserAccount) {
            return userAccountService.getFromAuthentication(authentication, UserAccount.class).toView();
        }
        else {
            throw new AccessDeniedException("You're not allowed to access this resource");
        }
    }
}
