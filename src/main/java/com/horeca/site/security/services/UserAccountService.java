package com.horeca.site.security.services;

import com.horeca.site.security.models.UserAccount;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserAccountService extends AbstractAccountService<UserAccount> {
}
