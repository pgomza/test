package com.horeca.site.security;

import com.horeca.site.exceptions.BadAuthenticationRequestException;
import com.horeca.site.security.models.AbstractAccount;
import com.horeca.site.security.services.LoginService;
import com.horeca.site.security.services.PasswordHashingService;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.LinkedHashMap;
import java.util.Map;

public class CustomTokenGranter extends AbstractTokenGranter {

    private static final String GRANT_TYPE = "password-like";
    private final LoginService loginService;

    private CustomTokenGranter(Builder builder) {
        super(builder.tokenServices, builder.clientDetailsService, builder.requestFactory, GRANT_TYPE);
        loginService = builder.loginService;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());
        final String clientId = client.getClientId();

        if (clientId.equals(OAuth2AuthorizationServerConfig.MOBILE_CLIENT_ID)) {
            // authenticate using the pin only
            String pin = parameters.get("pin");
            if (pin != null) {
                UserDetails userDetails = getUserDetails(AbstractAccount.MOBILE_CLIENT_USERNAME_PREFIX + pin);
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
                    userDetails = getUserDetails(AbstractAccount.PANEL_CLIENT_USERNAME_PREFIX + login, plainTextPassword);
                }
                else
                    userDetails = getUserDetails(AbstractAccount.SALES_CLIENT_USERNAME_PREFIX + login, plainTextPassword);

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

    public static class Builder {
        private AuthorizationServerTokenServices tokenServices;
        private ClientDetailsService clientDetailsService;
        private OAuth2RequestFactory requestFactory;
        private LoginService loginService;

        public Builder setTokenServices(AuthorizationServerTokenServices tokenServices) {
            this.tokenServices = tokenServices;
            return this;
        }

        public Builder setClientDetailsService(ClientDetailsService clientDetailsService) {
            this.clientDetailsService = clientDetailsService;
            return this;
        }

        public Builder setRequestFactory(OAuth2RequestFactory requestFactory) {
            this.requestFactory = requestFactory;
            return this;
        }

        public Builder setLoginService(LoginService loginService) {
            this.loginService = loginService;
            return this;
        }

        public CustomTokenGranter build() {
            return new CustomTokenGranter(this);
        }
    }
}
