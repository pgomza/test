package com.horeca.site.security;

import com.horeca.site.exceptions.BadAuthorizationRequestException;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.LinkedHashMap;
import java.util.Map;

public class CustomTokenGranter extends ResourceOwnerPasswordTokenGranter {

    // TODO the values should be passed, not hardcoded
    private final static String mobileClientId = "throdiMobile";
    private final static String panelClientId = "throdiPanel";

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

        if (client.getClientId().equals(mobileClientId)) { // authenticate using the pin only
            String pin = parameters.get("pin");
            if (pin != null) {
                UserInfo mobileUserInfo = getMobileUserInfo(UserInfo.AUTH_PREFIX_PIN + pin);
                if (mobileUserInfo == null)
                    throw new BadCredentialsException("Invalid pin");

                Authentication userAuth = getAsAuthenticated(mobileUserInfo, parameters);
                OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
                return new OAuth2Authentication(storedOAuth2Request, userAuth);
            }
            else
                throw new BadAuthorizationRequestException("A pin has to be specified in the request");
        }
        else if (client.getClientId().equals(panelClientId)) { // authenticate using the login and the password
            String login = parameters.get("login");
            String password = parameters.get("password"); //SHA-256

            if (login != null && password != null) {
                UserInfo panelUserInfo = getPanelUserInfo(UserInfo.AUTH_PREFIX_LOGIN + login, password);
                if (panelUserInfo == null)
                    throw new BadCredentialsException("Invalid login/password");

                Authentication userAuth = getAsAuthenticated(panelUserInfo, parameters);
                OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
                return new OAuth2Authentication(storedOAuth2Request, userAuth);
            }
            else
                throw new BadAuthorizationRequestException("Both a login and a password have to be specified in the request");
        }
        else // should never happen
            throw new BadAuthorizationRequestException("Unsupported type of client");

    }

    private Authentication getAsAuthenticated(UserInfo userInfo, Map<String, String> parameters) {
        final Authentication userAuth = new UsernamePasswordAuthenticationToken(userInfo, null, userInfo.getAuthorities());
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);

        return userAuth;
    }

    private UserInfo getMobileUserInfo(String username) {
        if (!loginService.isAlreadyPresent(username))
            return null;

        return loginService.loadUserByUsername(username);
    }

    private UserInfo getPanelUserInfo(String username, String password) {
        if (!loginService.isAlreadyPresent(username))
            return null;

        final UserInfo userInfo = loginService.loadUserByUsername(username);
        if (password.equals(userInfo.getPassword()))
            return userInfo;
        else
            return null;
    }
}
