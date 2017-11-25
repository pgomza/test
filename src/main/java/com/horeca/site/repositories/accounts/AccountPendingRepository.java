package com.horeca.site.repositories.accounts;

import com.horeca.site.models.accounts.AccountPending;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AccountPendingRepository<T extends AccountPending> extends CrudRepository<T, String> {

    T findBySecret(String secret);
}
