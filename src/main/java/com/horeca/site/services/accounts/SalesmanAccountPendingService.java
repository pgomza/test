package com.horeca.site.services.accounts;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.accounts.AccountPOST;
import com.horeca.site.models.accounts.AccountPending;
import com.horeca.site.models.accounts.SalesmanAccountPending;
import com.horeca.site.repositories.accounts.AccountPendingRepository;
import com.horeca.site.security.services.PasswordHashingService;
import com.horeca.site.security.services.SalesmanAccountService;
import com.horeca.site.services.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class SalesmanAccountPendingService extends AccountPendingService<SalesmanAccountPending> {

    private final SalesmanAccountService accountService;

    @Autowired
    public SalesmanAccountPendingService(EmailSenderService emailSenderService,
                                         AccountPendingRepository<SalesmanAccountPending> pendingRepository,
                                         @Value("${activation.url.salesmen}") String activationUrl,
                                         @Value("${activation.redirectionUrl}") String redirectionUrl,
                                         SalesmanAccountService accountService) {
        super(emailSenderService, pendingRepository, activationUrl, redirectionUrl);
        this.accountService = accountService;
    }

    public AccountPending add(AccountPOST accountPOST) {
        String email = accountPOST.getEmail();
        String plainTextPassword = accountPOST.getPassword();

        if (accountService.exists(accountPOST.getEmail())) {
            throw new BusinessRuleViolationException("A salesman with such an email already exists");
        }

        String hashedPassword = PasswordHashingService.getHashedFromPlain(plainTextPassword);
        String secret = UUID.randomUUID().toString();

        SalesmanAccountPending accountPending = new SalesmanAccountPending(email, hashedPassword, secret);
        return repository.save(accountPending);
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
