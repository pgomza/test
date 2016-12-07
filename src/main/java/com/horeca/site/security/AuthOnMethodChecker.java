package com.horeca.site.security;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthOnMethodChecker {

    public boolean isGuestAuthorized(OAuth2Authentication authentication, String pin) {
        System.out.println("im here");
        System.out.println(authentication);
        return true;
    }
}
