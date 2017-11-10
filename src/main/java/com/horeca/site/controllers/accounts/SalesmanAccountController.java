package com.horeca.site.controllers.accounts;

import com.horeca.site.models.accounts.SalesmanAccountPOST;
import com.horeca.site.models.accounts.SalesmanAccountView;
import com.horeca.site.security.services.SalesmanAccountService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(value = "hotels")
@RestController
@RequestMapping("/api/accounts")
public class SalesmanAccountController {

    @Autowired
    private SalesmanAccountService service;

    @RequestMapping(value = "/salesmen", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public SalesmanAccountView create(@RequestBody @Valid SalesmanAccountPOST accountPOST) {
        return service.create(accountPOST.getLogin(), accountPOST.getPassword()).toView();
    }
}
