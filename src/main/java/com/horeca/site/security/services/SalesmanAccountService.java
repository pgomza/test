package com.horeca.site.security.services;

import com.horeca.site.security.models.SalesmanAccount;
import com.horeca.site.security.repositories.SalesmanAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SalesmanAccountService extends AbstractAccountService<SalesmanAccount> {

    @Autowired
    private SalesmanAccountRepository repository;

    @Override
    protected SalesmanAccountRepository getRepository() {
        return repository;
    }
}
