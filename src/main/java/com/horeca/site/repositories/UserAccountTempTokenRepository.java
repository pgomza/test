package com.horeca.site.repositories;

import com.horeca.site.security.models.UserAccountTempToken;
import org.springframework.data.repository.CrudRepository;

public interface UserAccountTempTokenRepository extends CrudRepository<UserAccountTempToken, String> {
}
