package com.horeca.site.repositories;

import com.horeca.site.security.models.UserAccount;
import org.springframework.data.repository.CrudRepository;

public interface UserAccountRepository extends CrudRepository<UserAccount, String> {
}
