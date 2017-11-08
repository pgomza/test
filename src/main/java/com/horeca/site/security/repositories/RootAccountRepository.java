package com.horeca.site.security.repositories;

import com.horeca.site.security.models.RootAccount;
import org.springframework.data.repository.CrudRepository;

public interface RootAccountRepository extends CrudRepository<RootAccount, String> {
}
