package com.horeca.site.security.repositories;

import com.horeca.site.security.models.SalesmanAccount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SalesmanAccountRepository extends PagingAndSortingRepository<SalesmanAccount, String> {

    @Query("select count(*) from SalesmanAccount")
    Long getTotalCount();
}
