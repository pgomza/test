package com.horeca.site.security;

import com.horeca.site.models.user.UserInfo;
import com.horeca.site.repositories.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("persistentLoginService")
public class PersistentLoginService implements LoginService {

    @Autowired
    private UserInfoRepository repository;

    @Override
    public void saveUser(UserInfo user) {
        repository.save(user);
    }

    @Override
    public boolean isAlreadyPresent(String username) {
        return repository.exists(username);
    }

    @Override
    public UserInfo loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo userInfo = repository.findOne(username);
        if (userInfo == null)
            throw new UsernameNotFoundException("Person " + username + " could not be found");

        return userInfo;
    }
}
