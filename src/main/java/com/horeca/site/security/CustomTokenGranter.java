package com.horeca.site.security;

import com.horeca.site.exceptions.IncorrectProviderRequestException;
import com.horeca.site.exceptions.UnsupportedSocialProviderException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
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
        String provider = parameters.get("provider");
        if (provider == null) { //standard authentication
            return super.getOAuth2Authentication(client, tokenRequest);
        }
        else {
            if (!SUPPORTED_PROVIDERS.contains(provider.toUpperCase()))
                throw new UnsupportedSocialProviderException("Unsupported social provider: " + provider);

            String userId = parameters.get("userid");
            String token = parameters.get("token");

            if (userId == null || token == null || userId.isEmpty() || token.isEmpty())
                throw new IncorrectProviderRequestException("Incorrect request for social provider authentication");

            //TODO validate the token at the provider's site
            //assume the credentials have been verified as being correct
            Authentication userAuth = getAsAuthenticated(userId, parameters);
            OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
            return new OAuth2Authentication(storedOAuth2Request, userAuth);
        }
    }

    private Authentication getAsAuthenticated(final String userId, Map<String, String> parameters) {
        if (!loginService.isAlreadyPresent(userId)) {
            Collection<SimpleGrantedAuthority> authorities = Collections.unmodifiableCollection(Arrays.asList(new SimpleGrantedAuthority("ROLE_SOCIAL")));
            String randomPassword = UUID.randomUUID().toString();
            UserDetails userDetails = new User(userId, randomPassword, authorities);
            loginService.saveUser(userDetails);
        }

        final UserDetails userDetails = loginService.loadUserByUsername(userId);
        final Authentication userAuth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);

        return userAuth;
    }
}
