package com.horeca.site.services;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.models.user.UserInfo;
import com.horeca.site.repositories.StayRepository;
import com.horeca.site.security.LoginService;
import com.horeca.site.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class StayService {

    @Autowired
    private StayRepository stayRepository;

    @Autowired
    private LoginService loginService;

    @Autowired
    private PinGeneratorService pinGeneratorService;

    @Value("${hardcodedPin}")
    private String hardcodedPin;

    @PostConstruct
    public void initStays() {
        Stay byPin = stayRepository.findOne(hardcodedPin);
        if (byPin == null) {
            Stay stay = new Stay();
            stay.setPin(hardcodedPin);
            stay.setSomeInfo("Some information about the stay");
            stayRepository.save(stay);
            saveUserAssociatedWithPin(hardcodedPin);
        }
    }

    public Iterable<Stay> getAll() {
        return stayRepository.findAll();
    }

    @PreAuthorize("authentication.userAuthentication.details['pin'] == #pin")
    public Stay get(String pin) {
        ensureEntityExists(pin);
        ensureStatusActive(pin);
        return stayRepository.findOne(pin);
    }

    @PreAuthorize("authentication.userAuthentication.details['pin'] == #pin")
    public Stay update(String pin, Stay stay) {
        ensureEntityExists(pin);
        ensureStatusActive(pin);
        stay.setPin(pin);
        return stayRepository.save(stay);
    }

    @PreAuthorize("authentication.userAuthentication.details['pin'] == #pin")
    public void delete(String pin) {
        ensureEntityExists(pin);
        stayRepository.delete(pin);
    }

    @PreAuthorize("authentication.userAuthentication.details['pin'] == #pin")
    public Stay checkIn(String pin) {
        ensureEntityExists(pin);
        ensureStatusNew(pin);
        Stay stay = stayRepository.findOne(pin);
        stay.setStatus(Stay.Status.ACTIVE);
        Stay changed = stayRepository.save(stay);
        return changed;
    }

    @PreAuthorize("authentication.userAuthentication.details['pin'] == #pin")
    public void checkOut(String pin) {
        ensureEntityExists(pin);
        ensureStatusActive(pin);
        Stay stay = stayRepository.findOne(pin);
        stay.setStatus(Stay.Status.FINISHED);
        Stay changed = stayRepository.save(stay);
    }

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

    private void ensureEntityExists(String pin) {
        boolean exists = stayRepository.exists(pin);
        if (!exists)
            throw new ResourceNotFoundException();
    }

    private void ensureStatusActive(String pin) {
        Stay stay = stayRepository.findOne(pin);
        if (stay.getStatus() == Stay.Status.NEW)
            throw new BusinessRuleViolationException("You have to check in first");
        else if (stay.getStatus() == Stay.Status.FINISHED)
            throw new BusinessRuleViolationException("This stay is no longer active");
    }

    private void ensureStatusNew(String pin) {
        Stay stay = stayRepository.findOne(pin);
        if (stay.getStatus() == Stay.Status.ACTIVE)
            throw new BusinessRuleViolationException("You have already checked in");
        else if (stay.getStatus() == Stay.Status.FINISHED)
            throw new BusinessRuleViolationException("This stay is no longer active");
    }
}
