package com.horeca.site.security.services;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.security.models.AbstractAccount;
import com.horeca.site.security.models.SalesmanAccount;
import com.horeca.site.security.repositories.SalesmanAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    public SalesmanAccount getFromAuthentication(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof SalesmanAccount) {
            SalesmanAccount salesmanAccount = (SalesmanAccount) principal;
            // this is necessary because the instance obtained from 'authentication' isn't fully initialized
            return get(salesmanAccount.getLogin());
        }
        else
            throw new AccessDeniedException("Access denied");
    }

    public SalesmanAccount create(String login, String plainPassword) {
        if (exists(login)) {
            throw new BusinessRuleViolationException("Such a salesman already exists");
        }
        String hashedPassword = PasswordHashingService.getHashedFromPlain(plainPassword);
        SalesmanAccount account = new SalesmanAccount(AbstractAccount.SALES_CLIENT_USERNAME_PREFIX + login, hashedPassword);
        return save(account);
    }
}
