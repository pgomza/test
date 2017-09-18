package com.horeca.site.security.repositories;

import com.horeca.site.security.models.GuestAccount;
import org.springframework.data.repository.CrudRepository;

public interface GuestAccountRepository extends CrudRepository<GuestAccount, String> {
}
