package com.horeca.site.services.services;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.guest.Guest;
import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.stay.*;
import com.horeca.site.repositories.services.StayRepository;
import com.horeca.site.security.models.GuestAccount;
import com.horeca.site.security.services.GuestAccountService;
import com.horeca.site.services.GuestService;
import com.horeca.site.services.HotelService;
import com.horeca.site.services.PinGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class StayService {

    @Autowired
    private StayRepository stayRepository;

    @Autowired
    private GuestAccountService guestAccountService;

    @Autowired
    private PinGeneratorService pinGeneratorService;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private GuestService guestService;

    // used when the information about a given stay has to be returned
    // irrespective of the fact whether it's active or not; for 'internal'
    // use only
    public Stay getWithoutCheckingStatus(String pin) {
        ensureEntityExists(pin);
        return stayRepository.findOne(pin);
    }

    public Stay get(String pin) {
        ensureEntityExists(pin);
        ensureStatusNotNew(pin);
        return stayRepository.findOne(pin);
    }

    public StayView getView(String pin) {
        return get(pin).toView();
    }

    public Iterable<Stay> getAll() {
        return stayRepository.findAll();
    }

    @PostFilter("@accessChecker.checkForStayFromCollection(authentication, filterObject)")
    public Iterable<StayView> getAllViews() {
        Iterable<Stay> stays = getAll();
        List<StayView> views = new ArrayList<>();
        for (Stay stay : stays) {
            views.add(stay.toView());
        }
        return views;
    }

    public Collection<String> getByHotelId(Long hotelId) {
        return stayRepository.findByHotelId(hotelId);
    }

    public Stay update(String pin, Stay stay) {
        ensureEntityExists(pin);
        stay.setPin(pin);
        return stayRepository.save(stay);
    }

    public StayStatusUPDATE updateStatus(String pin, StayStatusUPDATE newStatus) {
        Stay stay = stayRepository.findOne(pin);
        stay.setStatus(newStatus.getStatus());
        stayRepository.save(stay);
        return newStatus;
    }

    public void delete(String pin) {
        ensureEntityExists(pin);
        deregisterStay(pin);
        stayRepository.delete(pin);
    }

    public void deleteAll() {
        stayRepository.deleteAll();
    }

    public StayView checkIn(String pin) {
        ensureEntityExists(pin);
        ensureStatusNotFinished(pin);
        Stay stay = stayRepository.findOne(pin);
        stay.setStatus(StayStatus.ACTIVE);

        Stay savedStay = stayRepository.save(stay);
        return savedStay.toView();
    }

    public void checkOut(String pin) {
        ensureEntityExists(pin);
        ensureStatusNotNew(pin);
        Stay stay = stayRepository.findOne(pin);
        stay.setStatus(StayStatus.FINISHED);
        stayRepository.save(stay);
    }

    @PreAuthorize("@accessChecker.checkAddingStay(authentication, #stayPOST)")
    public Stay registerNewStay(StayPOST stayPOST) {
        Stay stay = new Stay();
        stay.setStatus(StayStatus.NEW);
        stay.setRoomNumber(stayPOST.getRoomNumber());
        stay.setWifiPassword(stayPOST.getWifiPassword());
        stay.setDoorKey(stayPOST.getDoorKey());
        stay.setFromDate(stayPOST.getFromDate());
        stay.setToDate(stayPOST.getToDate());

        Hotel hotel = hotelService.get(stayPOST.getHotelId());
        Guest guest = guestService.get(stayPOST.getHotelId(), stayPOST.getGuestId());

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
        guestAccountService.registerGuest(stay);

        return added;
    }

    private void deregisterStay(String pin) {
        try {
            guestAccountService.delete(GuestAccount.USERNAME_PREFIX + pin);
        }
        catch (UsernameNotFoundException ex) {
            throw new RuntimeException("No corresponding token has been found for this stay in the database");
        }
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
