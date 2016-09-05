package com.horeca.site.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface LoginService extends UserDetailsService {

    void saveUser(UserDetails user);

    boolean isAlreadyPresent(String userId);
}
