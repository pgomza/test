package com.horeca.site.security.services;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.security.models.AbstractAccount;
import com.horeca.site.security.models.SalesmanAccount;
import com.horeca.site.security.repositories.SalesmanAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class SalesmanAccountService extends AbstractAccountService<SalesmanAccount> {

    @Autowired
    private SalesmanAccountRepository repository;

    @Override
    protected SalesmanAccountRepository getRepository() {
        return repository;
    }

    @Override
    protected String loginToUsername(String login) {
        return AbstractAccount.SALES_CLIENT_USERNAME_PREFIX + login;
    }

    public List<SalesmanAccount> getAll() {
        List<SalesmanAccount> result = new ArrayList<>();
        getRepository().findAll().forEach(result::add);
        result.sort(Comparator.comparing(AbstractAccount::getUsername));
        return result;
    }

    public SalesmanAccount create(String login, String plainPassword) {
        if (exists(login)) {
            throw new BusinessRuleViolationException("Such a salesman already exists");
        }
        String hashedPassword = PasswordHashingService.getHashedFromPlain(plainPassword);
        SalesmanAccount account = new SalesmanAccount(AbstractAccount.SALES_CLIENT_USERNAME_PREFIX + login,
                hashedPassword, Collections.singletonList(SalesmanAccount.DEFAULT_ROLE));
        return save(account);
    }
}
