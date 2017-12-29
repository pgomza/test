package com.horeca.site.controllers.accounts;

import com.horeca.site.models.accounts.RootAccountView;
import com.horeca.site.security.models.RootAccount;
import com.horeca.site.security.services.RootAccountService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "hotels")
@RestController
@RequestMapping("/api/accounts")
public class RootAccountController {

    @Autowired
    private RootAccountService service;

    @RequestMapping(value = "/roots/current", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public RootAccountView getCurrentView(Authentication authentication) {
        return service.getFromAuthentication(authentication, RootAccount.class).toView();
    }
}
