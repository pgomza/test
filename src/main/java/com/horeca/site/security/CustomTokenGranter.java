package com.horeca.site.security;

import com.horeca.site.exceptions.BadAuthenticationRequestException;
import com.horeca.site.security.models.GuestAccount;
import com.horeca.site.security.models.SalesmanAccount;
import com.horeca.site.security.models.UserAccount;
import com.horeca.site.security.services.LoginService;
import com.horeca.site.security.services.PasswordHashingService;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.LinkedHashMap;
import java.util.Map;

public class CustomTokenGranter extends ResourceOwnerPasswordTokenGranter {

    private static final String GRANT_TYPE = "password-like";
    private final LoginService loginService;
    private final AuthenticationManager authenticationManager;

    public CustomTokenGranter(AuthenticationManager authenticationManager, AuthorizationServerTokenServices tokenServices,
                              ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory,
                              LoginService loginService) {
        this(authenticationManager, tokenServices, clientDetailsService, requestFactory, GRANT_TYPE, loginService);
    }

    protected CustomTokenGranter(AuthenticationManager authenticationManager, AuthorizationServerTokenServices tokenServices,
                                 ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory,
                                 String grantType, LoginService loginService) {
        super(authenticationManager, tokenServices, clientDetailsService, requestFactory, grantType);
        this.authenticationManager = authenticationManager;
        this.loginService = loginService;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap<String, String>(tokenRequest.getRequestParameters());
        final String clientId = client.getClientId();

        if (clientId.equals(OAuth2AuthorizationServerConfig.MOBILE_CLIENT_ID)) {
            // authenticate using the pin only
            String pin = parameters.get("pin");
            if (pin != null) {
                UserDetails userDetails = getUserDetails(GuestAccount.USERNAME_PREFIX + pin);
                if (userDetails == null) {
                    throw new BadCredentialsException("Invalid pin");
                }
                else if (!userDetails.isEnabled()) {
                    throw new DisabledException("The account is disabled");
                }

                Authentication userAuth = getAsAuthenticated(userDetails, parameters);
                OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
                return new OAuth2Authentication(storedOAuth2Request, userAuth);
            }
            else {
                throw new BadAuthenticationRequestException("The pin has to be specified in the request");
            }
        }
        else if (clientId.equals(OAuth2AuthorizationServerConfig.PANEL_CLIENT_ID)
                || clientId.equals(OAuth2AuthorizationServerConfig.SALES_CLIENT_ID)) {
            // authenticate using the login and the password
            String login = parameters.get("login");
            String plainTextPassword = parameters.get("password");

            if (login != null && plainTextPassword != null) {
                UserDetails userDetails;
                if (clientId.equals(OAuth2AuthorizationServerConfig.PANEL_CLIENT_ID)) {
                    userDetails = getUserDetails(UserAccount.USERNAME_PREFIX + login, plainTextPassword);
                }
                else
                    userDetails = getUserDetails(SalesmanAccount.USERNAME_PREFIX + login, plainTextPassword);

                if (userDetails == null) {
                    throw new BadCredentialsException("Invalid login/password");
                }
                else if (!userDetails.isEnabled()) {
                    throw new DisabledException("The account is disabled");
                }

                Authentication userAuth = getAsAuthenticated(userDetails, parameters);
                OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
                return new OAuth2Authentication(storedOAuth2Request, userAuth);
            }
            else {
                throw new BadAuthenticationRequestException("Both the login and the password have to be specified in the request");
            }
        }
        else {
            // should never happen
            throw new BadAuthenticationRequestException("Unsupported type of client");
        }
    }

    private Authentication getAsAuthenticated(UserDetails userDetails, Map<String, String> parameters) {
        final Authentication userAuth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);

        return userAuth;
    }

    private UserDetails getUserDetails(String username) {
        if (!loginService.exists(username)) {
            return null;
        }

        return loginService.loadUserByUsername(username);
    }

    private UserDetails getUserDetails(String username, String plainTextPassword) {
        if (!loginService.exists(username)) {
            return null;
        }

        final UserDetails userDetails = loginService.loadUserByUsername(username);
        if (PasswordHashingService.checkIfPlainEqualToHashed(plainTextPassword, userDetails.getPassword())) {
            return userDetails;
        }
        else {
            return null;
        }
    }
}
