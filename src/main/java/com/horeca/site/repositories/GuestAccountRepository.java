package com.horeca.site.repositories;

import com.horeca.site.security.GuestAccount;
import org.springframework.data.repository.CrudRepository;

public interface GuestAccountRepository extends CrudRepository<GuestAccount, String> {
}
