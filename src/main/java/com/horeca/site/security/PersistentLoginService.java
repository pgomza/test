package com.horeca.site.security;

import com.horeca.site.repositories.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("persistentLoginService")
@Transactional
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
            throw new UsernameNotFoundException("User " + username + " could not be found");

        return userInfo;
    }

    @Override
    public void deleteUser(String username) throws UsernameNotFoundException {
        boolean exists = repository.exists(username);
        if (!exists)
            throw new UsernameNotFoundException("User " + username + " could not be found");

        repository.delete(username);
    }
}
