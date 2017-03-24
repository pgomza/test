package com.horeca.site.security.services;

import com.horeca.site.security.models.UserAccount;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class UserAccountService extends AbstractAccountService<UserAccount> {

    public Set<UserAccount> getAll() {
        Set<UserAccount> userAccounts = new HashSet<>();
        for (UserAccount account : repository.findAll()) {
            userAccounts.add(account);
        }
        return userAccounts;
    }

    public UserAccount save(UserAccount userAccount) {
        return repository.save(userAccount);
    }
}
