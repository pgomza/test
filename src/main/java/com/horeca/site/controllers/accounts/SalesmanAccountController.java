package com.horeca.site.controllers.accounts;

import com.horeca.site.models.accounts.SalesmanAccountPOST;
import com.horeca.site.models.accounts.SalesmanAccountView;
import com.horeca.site.security.models.SalesmanAccount;
import com.horeca.site.security.services.SalesmanAccountService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

    @RequestMapping(value = "/salesmen/{login}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public SalesmanAccountView get(@PathVariable("login") String login) {
        return service.get(login).toView();
    }

    @RequestMapping(value = "/salesmen", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public SalesmanAccountView create(@RequestBody @Valid SalesmanAccountPOST accountPOST) {
        return service.create(accountPOST.getLogin(), accountPOST.getPassword()).toView();
    }
}
