package com.horeca.site.services.accounts;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.accounts.AccountPOST;
import com.horeca.site.models.accounts.AccountPending;
import com.horeca.site.models.accounts.SalesmanAccountPending;
import com.horeca.site.repositories.accounts.SalesmanAccountPendingRepository;
import com.horeca.site.security.services.PasswordHashingService;
import com.horeca.site.security.services.SalesmanAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class SalesmanAccountPendingService extends AccountPendingService {

    @Autowired
    private SalesmanAccountPendingRepository repository;

    @Autowired
    private SalesmanAccountService accountService;

    @Value("${activation.url.salesmen}")
    private String activationUrl;

    @Override
    protected String getActivationUrl() {
        return activationUrl;
    }

    @Override
    protected SalesmanAccountPendingRepository getRepository() {
        return repository;
    }

    @Override
    public AccountPending add(AccountPOST accountPOST) {
        String email = accountPOST.getEmail();
        String plainTextPassword = accountPOST.getPassword();

        if (accountService.exists(accountPOST.getEmail())) {
            throw new BusinessRuleViolationException("A salesman with such an email already exists");
        }

        String hashedPassword = PasswordHashingService.getHashedFromPlain(plainTextPassword);
        String secret = UUID.randomUUID().toString();

        SalesmanAccountPending accountPending = new SalesmanAccountPending(email, hashedPassword, secret);
        return getRepository().save(accountPending);
    }

    @Override
    public void activate(String secret) {
        AccountPending pending;
        try {
            pending = getBySecret(secret);
        } catch (ResourceNotFoundException ex) {
            throw new BusinessRuleViolationException("Invalid secret");
        }

        accountService.create(pending.getEmail(), pending.getPassword());
        repository.delete(pending.getEmail());
    }
}
