package com.horeca.site.repositories;

import com.horeca.site.models.user.UserInfo;
import org.springframework.data.repository.CrudRepository;

public interface UserInfoRepository extends CrudRepository<UserInfo, String> {
}
