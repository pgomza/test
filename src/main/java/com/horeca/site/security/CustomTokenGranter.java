package com.horeca.site.security;

import com.horeca.site.exceptions.BadAuthorizationRequestException;
import com.horeca.site.models.UserInfo;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.*;

public class CustomTokenGranter extends ResourceOwnerPasswordTokenGranter {

    private static final String GRANT_TYPE;
    private static final Set<String> SUPPORTED_PROVIDERS;

    static {
        GRANT_TYPE = "password-like";
        SUPPORTED_PROVIDERS = Collections.unmodifiableSet(new HashSet<String>(
                Arrays.asList("FACEBOOK", "TWITTER", "GOOGLE")
        ));
    }

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
        String pin = parameters.get("pin");
        if (pin != null) {
            Authentication userAuth = getAsAuthenticated(UserInfo.AUTH_PREFIX_PIN + pin, parameters);
            if (userAuth == null)
                throw new RuntimeException("Invalid pin");
            OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
            return new OAuth2Authentication(storedOAuth2Request, userAuth);
        }

        //TODO add handling authentication via the standard account or social providers
        throw new BadAuthorizationRequestException("You have not included pin in your request");
    }

    private Authentication getAsAuthenticated(final String pin, Map<String, String> parameters) {

        if (!loginService.isAlreadyPresent(pin))
            return null;

        final UserInfo userInfo = loginService.loadUserByUsername(pin);
        final Authentication userAuth = new UsernamePasswordAuthenticationToken(userInfo, null, userInfo.getAuthorities());
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);

        return userAuth;
    }
}
