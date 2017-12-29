package com.horeca.site.security.services;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.models.accounts.SalesmanAccountView;
import com.horeca.site.security.OAuth2AuthorizationServerConfig;
import com.horeca.site.security.models.AbstractAccount;
import com.horeca.site.security.models.SalesmanAccount;
import com.horeca.site.security.repositories.SalesmanAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    protected String getOAuthClientId() {
        return OAuth2AuthorizationServerConfig.SALES_CLIENT_ID;
    }

    @Override
    public boolean exists(String login) {
        return "current".equals(login) || super.exists(login);
    }

    public List<SalesmanAccount> getAll() {
        List<SalesmanAccount> result = new ArrayList<>();
        getRepository().findAll().forEach(result::add);
        result.sort(Comparator.comparing(AbstractAccount::getUsername));
        return result;
    }

    public Page<SalesmanAccountView> getViews(Pageable pageable) {
        Page<SalesmanAccount> pageOfAccounts = getRepository().findAll(pageable);
        List<SalesmanAccountView> accountViews = pageOfAccounts.getContent().stream()
                .map(SalesmanAccount::toView)
                .collect(Collectors.toList());
        return new PageImpl<>(accountViews, pageable, getRepository().getTotalCount());
    }

    public SalesmanAccount create(String login, String password, boolean isPasswordAlreadyHashed) {
        if (exists(login)) {
            throw new BusinessRuleViolationException("Such a salesman account already exists");
        }

        String hashedPassword;
        if (isPasswordAlreadyHashed) {
            hashedPassword = password;
        }
        else {
            hashedPassword = PasswordHashingService.getHashedFromPlain(password);
        }

        SalesmanAccount account = new SalesmanAccount(loginToUsername(login), hashedPassword);
        return save(account);
    }
}
