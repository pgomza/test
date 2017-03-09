package com.horeca.site.security;

import com.horeca.site.repositories.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserAccountService {

    @Autowired
    private UserAccountRepository repository;

    public UserAccount save(UserAccount account) {
        return repository.save(account);
    }
}
