package com.horeca.site.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InMemoryLoginService implements LoginService {

    private final Map<String, UserDetails> database = new HashMap<>();

    @PostConstruct
    public void init() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        saveUser(new User("user1", "password1", authorities));
    }

    @Override
    public void saveUser(UserDetails user) {
        database.put(user.getUsername(), user);
    }

    @Override
    public boolean isAlreadyPresent(String userId) {
        return database.containsKey(userId);
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        UserDetails userDetails = database.get(userId);
        if (userDetails == null) {
            throw new UsernameNotFoundException("Person " + userId + " could not be found");
        }
        return new User(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
    }
}
