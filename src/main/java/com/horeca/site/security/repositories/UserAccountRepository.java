package com.horeca.site.security.repositories;

import com.horeca.site.security.models.UserAccount;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserAccountRepository extends CrudRepository<UserAccount, String> {

    List<UserAccount> findAllByHotelId(Long hotelId);
}
