package com.horeca.site.services.accounts;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.accounts.UserAccountPending;
import com.horeca.site.repositories.accounts.UserAccountPendingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAccountPendingService {

    @Autowired
    protected UserAccountPendingRepository repository;

    public boolean exists(String email) {
        return repository.exists(email);
    }

    public UserAccountPending get(String email) {
        return repository.findOne(email);
    }

    public UserAccountPending getBySecret(String secret) {
        UserAccountPending pending = repository.findBySecret(secret);
        if (pending == null) {
            throw new ResourceNotFoundException();
        }
        return pending;
    }

    public UserAccountPending save(UserAccountPending account) {
        return repository.save(account);
    }

    public void delete(String email) {
        repository.delete(email);
    }
}
