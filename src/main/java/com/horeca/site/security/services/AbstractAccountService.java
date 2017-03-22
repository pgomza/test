package com.horeca.site.security.services;

import com.horeca.site.security.models.AbstractAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

public abstract class AbstractAccountService<T extends AbstractAccount> {

    @Autowired
    protected CrudRepository<T, String> repository;

    public boolean exists(String username) {
        return repository.exists(username);
    }

    public T save(T account) {
        return repository.save(account);
    }

    public void delete(String username) {
        repository.delete(username);
    }
}
