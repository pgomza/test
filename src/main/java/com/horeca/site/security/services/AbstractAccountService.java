package com.horeca.site.security.services;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.security.models.AbstractAccount;
import org.springframework.data.repository.CrudRepository;

public abstract class AbstractAccountService<T extends AbstractAccount> {

    final static String PASSWORD_REGEX = "[^\\s]{5,}";

    abstract protected CrudRepository<T, String> getRepository();

    abstract public boolean exists(String login);

    abstract public T get(String login) throws ResourceNotFoundException;

    abstract public void delete(String login);

    public T save(T account) {
        return getRepository().save(account);
    }

    void disable(T account) {
        account.setEnabled(false);
        save(account);
    }

    void enable(T account) {
        account.setEnabled(true);
        save(account);
    }
}
