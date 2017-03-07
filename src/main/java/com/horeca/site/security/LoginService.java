package com.horeca.site.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface LoginService extends UserDetailsService {

    boolean exists(String username);

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
