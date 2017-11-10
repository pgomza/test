package com.horeca.site.security.services;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.security.models.AbstractAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

public abstract class AbstractAccountService<T extends AbstractAccount> {

    final static String PASSWORD_REGEX = "[^\\s]{5,}";

    @Autowired
    private LoginService loginService;

    abstract protected CrudRepository<T, String> getRepository();

    abstract protected String loginToUsername(String login);

    public boolean exists(String login) {
        return loginService.exists(loginToUsername(login));
    }

    public T get(String login) {
        T account = getRepository().findOne(loginToUsername(login));
        if (account == null) {
            throw new ResourceNotFoundException();
        }
        return account;
    }

    public void delete(String login) {
        getRepository().delete(loginToUsername(login));
    }

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
