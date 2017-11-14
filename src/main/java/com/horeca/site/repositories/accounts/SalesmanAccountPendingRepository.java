package com.horeca.site.repositories.accounts;

import com.horeca.site.models.accounts.SalesmanAccountPending;
import org.springframework.data.repository.CrudRepository;

public interface SalesmanAccountPendingRepository extends CrudRepository<SalesmanAccountPending, String> {

    SalesmanAccountPending findBySecret(String secret);
}
