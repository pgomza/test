package com.horeca.site.security.services;

import com.horeca.site.repositories.UserAccountRepository;
import com.horeca.site.security.models.UserAccount;
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
