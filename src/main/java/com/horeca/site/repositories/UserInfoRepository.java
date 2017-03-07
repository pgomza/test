package com.horeca.site.repositories;

import com.horeca.site.security.UserInfo;
import org.springframework.data.repository.CrudRepository;

public interface UserInfoRepository extends CrudRepository<UserInfo, String> {
}
