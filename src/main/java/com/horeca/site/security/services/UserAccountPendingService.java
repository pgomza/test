package com.horeca.site.security.services;

import com.horeca.site.repositories.UserAccountPendingRepository;
import com.horeca.site.security.models.UserAccountPending;
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

    public UserAccountPending save(UserAccountPending account) {
        return repository.save(account);
    }

    public void delete(String email) {
        repository.delete(email);
    }
}
