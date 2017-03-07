package com.horeca.site.security;

import com.horeca.site.models.stay.Stay;
import com.horeca.site.repositories.GuestAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GuestAccountService {

    @Autowired
    private GuestAccountRepository repository;

    public GuestAccount save(GuestAccount account) {
        return repository.save(account);
    }

    public void delete(String username) {
        repository.delete(username);
    }

    public void registerGuest(Stay stay) {
        GuestAccount guestAccount = new GuestAccount(GuestAccount.USERNAME_PREFIX + stay.getPin());
        save(guestAccount);
    }
}
