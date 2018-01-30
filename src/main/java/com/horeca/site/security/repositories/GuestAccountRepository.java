package com.horeca.site.security.repositories;

import com.horeca.site.security.models.GuestAccount;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface GuestAccountRepository extends PagingAndSortingRepository<GuestAccount, String> {
}
