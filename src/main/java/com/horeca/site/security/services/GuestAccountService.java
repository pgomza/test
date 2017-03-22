package com.horeca.site.security.services;

import com.horeca.site.models.stay.Stay;
import com.horeca.site.security.models.GuestAccount;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GuestAccountService extends AbstractAccountService<GuestAccount> {

    public void registerGuest(Stay stay) {
        GuestAccount guestAccount = new GuestAccount(GuestAccount.USERNAME_PREFIX + stay.getPin());
        save(guestAccount);
    }
}
