package com.horeca.site.services.services;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.stay.*;
import com.horeca.site.models.guest.Guest;
import com.horeca.site.models.user.UserInfo;
import com.horeca.site.repositories.services.StayRepository;
import com.horeca.site.security.LoginService;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.services.HotelService;
import com.horeca.site.services.PinGeneratorService;
import com.horeca.site.services.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class StayService {

    @Autowired
    private StayRepository stayRepository;

    @Autowired
    private LoginService loginService;

    @Autowired
    private PinGeneratorService pinGeneratorService;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private GuestService userService;

    public Iterable<Stay> getAll() {
        return stayRepository.findAll();
    }

    public Iterable<StayView> getAllViews(String preferredLanguage) {
        Set<StayView> views = new HashSet<>();
        for (Stay stay : getAll()) {
            views.add(stay.toView(preferredLanguage, stay.getHotel().getDefaultTranslation()));
        }
        return views;
    }

    @PreAuthorize("authentication.userAuthentication.details['pin'] == #pin")
//    @PreAuthorize("@authOnMethodChecker.isGuestAuthorized(authentication, #pin)")
    public Stay get(String pin) {
        ensureEntityExists(pin);
        ensureStatusNotNew(pin);
        return stayRepository.findOne(pin);
    }

    @PreAuthorize("authentication.userAuthentication.details['pin'] == #pin")
    public StayView getView(String pin, String preferredLanguage) {
        Stay stay = get(pin);
        return stay.toView(preferredLanguage, stay.getHotel().getDefaultTranslation());
    }

//    @PreAuthorize("authentication.userAuthentication.details['pin'] == #pin")
    public Stay update(String pin, Stay stay) {
        ensureEntityExists(pin);
//        ensureStatusNotNew(pin);
        stay.setPin(pin);
        return stayRepository.save(stay);
    }

    public StayStatusUPDATE updateStatus(String pin, StayStatusUPDATE newStatus) {
        Stay stay = stayRepository.findOne(pin);
        stay.setStatus(newStatus.getStatus());
        stayRepository.save(stay);
        return newStatus;
    }

//    @PreAuthorize("authentication.userAuthentication.details['pin'] == #pin")
    public void delete(String pin) {
        ensureEntityExists(pin);
        stayRepository.delete(pin);
    }

    public void deleteAll() {
        stayRepository.deleteAll();
    }

//    @PreAuthorize("authentication.userAuthentication.details['pin'] == #pin")
    public Stay checkIn(String pin) {
        ensureEntityExists(pin);
        ensureStatusNotFinished(pin);
        Stay stay = stayRepository.findOne(pin);
        stay.setStatus(StayStatus.ACTIVE);
        return stayRepository.save(stay);
    }

//    @PreAuthorize("authentication.userAuthentication.details['pin'] == #pin")
    public void checkOut(String pin) {
        ensureEntityExists(pin);
        ensureStatusNotNew(pin);
        Stay stay = stayRepository.findOne(pin);
        stay.setStatus(StayStatus.FINISHED);
        stayRepository.save(stay);
    }

    public Stay registerNewStay(StayPOST stayPOST) {
        Stay stay = new Stay();
        stay.setStatus(StayStatus.NEW);
        stay.setRoomNumber(stayPOST.getRoomNumber());
        stay.setFromDate(stayPOST.getFromDate());
        stay.setToDate(stayPOST.getToDate());

        Hotel hotel = hotelService.get(stayPOST.getHotelId());
        Guest guest = userService.get(stayPOST.getGuestId());

        if (hotel == null || guest == null)
            throw new ResourceNotFoundException();

        stay.setHotel(hotel);
        stay.setGuest(guest);

        return registerNewStay(stay);
    }

    private Stay registerNewStay(Stay stay) {
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

    private void ensureStatusNotNew(String pin) {
        Stay stay = stayRepository.findOne(pin);
        if (stay.getStatus() == StayStatus.NEW)
            throw new BusinessRuleViolationException("You have to check in first");
    }

    private void ensureStatusNotFinished(String pin) {
        Stay stay = stayRepository.findOne(pin);
        if (stay.getStatus() == StayStatus.FINISHED)
            throw new BusinessRuleViolationException("This stay is no longer active");
    }
}
