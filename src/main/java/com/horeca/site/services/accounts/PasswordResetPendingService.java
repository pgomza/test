package com.horeca.site.services.accounts;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.accounts.PasswordResetPending;
import com.horeca.site.repositories.accounts.PasswordResetPendingRepository;
import org.joda.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PasswordResetPendingService {

    @Autowired
    private PasswordResetPendingRepository repository;

    public PasswordResetPending save(PasswordResetPending pending) {
        PasswordResetPending currentlySaved = repository.findOne(pending.getAccount().getUsername());
        if (currentlySaved == null) {
            return repository.save(pending);
        }
        else {
            currentlySaved.setSecret(pending.getSecret());
            currentlySaved.setExpirationTimestamp(pending.getExpirationTimestamp());
            return repository.save(currentlySaved);
        }
    }

    public void delete(PasswordResetPending pending) {
        repository.delete(pending);
    }

    public PasswordResetPending getBySecret(String secret) {
        PasswordResetPending entity = repository.findBySecret(secret);
        if (entity == null) {
            throw new ResourceNotFoundException();
        }
        return entity;
    }

    public boolean isValid(PasswordResetPending pending) {
        long currentTimestamp = Instant.now().getMillis();
        return currentTimestamp < pending.getExpirationTimestamp();
    }

    public void deleteAllInvalid() {
        for (PasswordResetPending pending : repository.findAll()) {
            if (!isValid(pending)) {
                repository.delete(pending);
            }
        }
    }
}
