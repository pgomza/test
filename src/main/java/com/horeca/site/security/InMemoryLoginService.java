package com.horeca.site.security;

import com.horeca.site.models.UserInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

//@Service("inMemoryLoginService")
public class InMemoryLoginService implements LoginService {

    private final Map<String, UserInfo> database = new HashMap<>();

//    @PostConstruct
//    public void init() {
//        //add a test user
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
//        String randomPass = UUID.randomUUID().toString();
//        saveUser(new UserInfo(, randomPass, authorities));
//    }

    @Override
    public void saveUser(UserInfo user) {
        database.put(user.getUsername(), user);
    }

    @Override
    public boolean isAlreadyPresent(String username) {
        return database.containsKey(username);
    }

    @Override
    public UserInfo loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo userInfo = database.get(username);
        if (userInfo == null)
            throw new UsernameNotFoundException("Person " + username + " could not be found");

        return userInfo;
    }
}
