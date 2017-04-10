package com.horeca.site.repositories;

import com.horeca.site.security.models.UserAccountPending;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by wojt on 09.04.17.
 */
public interface UserAccountPendingRepository extends CrudRepository<UserAccountPending, String> {
}
