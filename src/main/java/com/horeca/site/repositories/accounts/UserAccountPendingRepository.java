package com.horeca.site.repositories.accounts;

import com.horeca.site.models.accounts.UserAccountPending;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by wojt on 09.04.17.
 */
public interface UserAccountPendingRepository extends CrudRepository<UserAccountPending, String> {

    UserAccountPending findBySecret(String secret);
}
