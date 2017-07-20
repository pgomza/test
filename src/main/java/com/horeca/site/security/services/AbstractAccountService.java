package com.horeca.site.security.services;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.security.models.AbstractAccount;
import org.springframework.data.repository.CrudRepository;

public abstract class AbstractAccountService<T extends AbstractAccount> {

    final static String PASSWORD_REGEX = "[^\\s]{5,}";

    abstract protected CrudRepository<T, String> getRepostiory();

    public boolean exists(String username) {
        return getRepostiory().exists(username);
    }

    public T get(String username) {
        T account = getRepostiory().findOne(username);
        if (account == null) {
            throw new ResourceNotFoundException("Could not find an account with such a username");
        }
        return account;
    }

    public T save(T account) {
        return getRepostiory().save(account);
    }

    public void delete(String username) {
        getRepostiory().delete(username);
    }

    public void disable(T account) {
        account.setEnabled(false);
        save(account);
    }
}
