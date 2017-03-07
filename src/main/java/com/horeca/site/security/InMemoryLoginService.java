package com.horeca.site.security;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashMap;
import java.util.Map;

//@Service("inMemoryLoginService")
public class InMemoryLoginService implements LoginService {

    private final Map<String, UserInfo> database = new HashMap<>();

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
            throw new UsernameNotFoundException("User " + username + " could not be found");

        return userInfo;
    }

    @Override
    public void deleteUser(String username) throws UsernameNotFoundException {
        boolean exists = database.containsKey(username);
        if (!exists)
            throw new UsernameNotFoundException("User " + username + " could not be found");

        database.remove(username);
    }
}
