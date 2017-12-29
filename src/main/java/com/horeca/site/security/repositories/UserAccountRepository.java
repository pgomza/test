package com.horeca.site.security.repositories;

import com.horeca.site.security.models.UserAccount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface UserAccountRepository extends PagingAndSortingRepository<UserAccount, String> {

    List<UserAccount> findAllByHotelId(Long hotelId);

    @Query("select count(*) from UserAccount")
    Long getTotalCount();
}
