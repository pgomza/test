package com.horeca.site.repositories;

import com.horeca.site.security.models.SalesmanAccount;
import org.springframework.data.repository.CrudRepository;

public interface SalesmanAccountRepository extends CrudRepository<SalesmanAccount, String> {
}
