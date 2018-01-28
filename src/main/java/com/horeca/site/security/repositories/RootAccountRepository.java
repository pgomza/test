package com.horeca.site.security.repositories;

import com.horeca.site.security.models.RootAccount;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RootAccountRepository extends PagingAndSortingRepository<RootAccount, String> {
}
