package com.horeca.site.services;

import com.horeca.site.models.Stay;
import com.horeca.site.models.UserInfo;
import com.horeca.site.repositories.StayRepository;
import com.horeca.site.security.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StayRegistrationService {

    @Autowired
    private StayRepository stayRepository;

    @Autowired
    @Qualifier("persistentLoginService")
    private LoginService loginService;

    @Autowired
    private PinGeneratorService pinGeneratorService;

    public Stay registerNewStay(Stay stay) {
        String pin = pinGeneratorService.generatePin();
        stay.setPin(pin);

        Stay added = stayRepository.save(stay);
        saveUserAssociatedWithPin(pin);

        return added;
    }

    private void saveUserAssociatedWithPin(String pin) {
        List<String> roles = new ArrayList<>(Arrays.asList("ROLE_USER"));
        String randomPassword = UUID.randomUUID().toString();
        UserInfo userInfo = new UserInfo(UserInfo.AUTH_PREFIX_PIN + pin, randomPassword, roles);
        loginService.saveUser(userInfo);
    }
}
