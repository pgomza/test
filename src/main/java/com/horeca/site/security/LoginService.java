package com.horeca.site.security;

import com.horeca.site.models.UserInfo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface LoginService extends UserDetailsService {

    void saveUser(UserInfo user);

    boolean isAlreadyPresent(String username);

    @Override
    UserInfo loadUserByUsername(String username) throws UsernameNotFoundException;
}
