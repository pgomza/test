package com.horeca.site.repositories.accounts;

import com.horeca.site.models.accounts.UserAccountTempToken;
import org.springframework.data.repository.CrudRepository;

public interface UserAccountTempTokenRepository extends CrudRepository<UserAccountTempToken, String> {
}
