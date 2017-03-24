package com.horeca.site.controllers;

import com.horeca.site.models.accounts.UserAccountView;
import com.horeca.site.security.models.UserAccountPOST;
import com.horeca.site.security.models.UserAccountTempTokenRequest;
import com.horeca.site.security.models.UserAccountTempTokenResponse;
import com.horeca.site.services.AccountService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Api(value = "hotels")
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService service;

    @RequestMapping(value = "/users", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserAccountView addUserAccount(@RequestHeader(name = "Temp-Token", required = true) String token,
                                                 @RequestBody UserAccountPOST userAccountPOST) {
        return service.addUserAccount(token, userAccountPOST);
    }

    @RequestMapping(value = "/users/current", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserAccountView getCurrentUserAccount(Authentication authentication) {
        return service.getCurrentUserAccount(authentication);
    }

    @RequestMapping(value = "/users/tokens", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserAccountTempTokenResponse getTempTokenForNewUserAccount(@RequestBody UserAccountTempTokenRequest request) {
        return service.getTempTokenForNewUserAccount(request);
    }

    @RequestMapping(value = "/users/tokens/{token}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserAccountTempTokenResponse getInfoAboutUserAccountTempToken(@PathVariable("token") String token) {
        return service.getInfoAboutUserAccountTempToken(token);
    }
}
