package com.horeca.site.security.services;

import com.horeca.site.repositories.SalesmanAccountRepository;
import com.horeca.site.security.models.SalesmanAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SalesmanAccountService {

    @Autowired
    private SalesmanAccountRepository repository;

    public SalesmanAccount save(SalesmanAccount account) {
        return repository.save(account);
    }
}
