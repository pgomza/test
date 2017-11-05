package com.horeca.site.services.services;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.guest.Guest;
import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.notifications.NewStayEvent;
import com.horeca.site.models.stay.*;
import com.horeca.site.repositories.services.StayRepository;
import com.horeca.site.security.models.GuestAccount;
import com.horeca.site.security.models.UserAccount;
import com.horeca.site.security.services.GuestAccountService;
import com.horeca.site.services.GuestService;
import com.horeca.site.services.HotelService;
import com.horeca.site.services.PinGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private ApplicationEventPublisher eventPublisher;

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

    public List<Stay> getAllWithStatuses(Set<StayStatus> statuses) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof GuestAccount) {
            GuestAccount guestAccount = (GuestAccount) principal;
            Stay stay = getWithoutCheckingStatus(guestAccount.getPin());
            if (statuses.contains(stay.getStatus())) {
                return Collections.singletonList(stay);
            }
        }
        else if (principal instanceof UserAccount) {
            UserAccount userAccount = (UserAccount) principal;
            Collection<String> stayPins = stayRepository.findByHotelIdAndStatuses(userAccount.getHotelId(), statuses);
            List<Stay> stays = new ArrayList<>();
            stayRepository.findAll(stayPins).forEach(stays::add);
            return stays;
        }

        return Collections.emptyList();
    }

    public List<Stay> getAll() {
        return getAllWithStatuses(new HashSet<>(Arrays.asList(StayStatus.values())));
    }

    public List<StayView> getAllWithStatusesViews(Set<StayStatus> statuses) {
        return getAllWithStatuses(statuses).stream().map(Stay::toView).collect(Collectors.toList());
    }

    public List<StayView> getAllViews() {
        return getAllWithStatusesViews(new HashSet<>(Arrays.asList(StayStatus.values())));
    }

    /*
        -------------------------------------------------
        query methods
     */

    public Collection<String> getByHotelId(Long hotelId) {
        return stayRepository.findByHotelId(hotelId);
    }

    public String getByCubilisId(Long cubilisId) {
        return stayRepository.findByCubilisId(cubilisId);
    }

    /*
        -------------------------------------------------
     */

    public Stay update(String pin, Stay updated) {
        ensureEntityExists(pin);
        updated.setPin(pin);
        return stayRepository.save(updated);
    }

    public Stay update(String pin, StayUPDATE updated) {
        Stay currentStay = getWithoutCheckingStatus(pin);
        currentStay.setFromDate(updated.getFromDate());
        currentStay.setToDate(updated.getToDate());
        currentStay.setRoomNumber(updated.getRoomNumber());
        currentStay.setWifiPassword(updated.getWifiPassword());
        currentStay.setDoorKey(updated.getDoorKey());
        currentStay.setStatus(updated.getStatus());

        return stayRepository.save(currentStay);
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

        stay.setHotel(hotel);
        stay.setGuest(guest);

        return registerNewStay(stay);
    }

    public void notifyGuestAboutStay(String pin) {
        Stay stay = getWithoutCheckingStatus(pin);
        eventPublisher.publishEvent(new NewStayEvent(this, stay));
    }

    public Stay registerNewStay(Stay stay) {
        String pin = pinGeneratorService.generatePin();
        stay.setPin(pin);

        Stay added = stayRepository.save(stay);
        guestAccountService.registerGuest(stay);

        return added;
    }

    private void deregisterStay(String pin) {
        try {
            guestAccountService.delete(pin);
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
