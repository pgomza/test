package com.horeca.site.repositories.accounts;

import com.horeca.site.models.accounts.PasswordResetPending;
import org.springframework.data.repository.CrudRepository;

public interface PasswordResetPendingRepository extends CrudRepository<PasswordResetPending, String> {
}
