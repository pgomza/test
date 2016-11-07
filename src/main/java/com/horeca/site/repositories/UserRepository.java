package com.horeca.site.repositories;

import com.horeca.site.models.user.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
