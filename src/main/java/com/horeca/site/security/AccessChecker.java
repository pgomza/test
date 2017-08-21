package com.horeca.site.security;

import com.horeca.site.models.guest.Guest;
import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.models.stay.StayPOST;
import com.horeca.site.models.stay.StayView;
import com.horeca.site.security.models.GuestAccount;
import com.horeca.site.security.models.SalesmanAccount;
import com.horeca.site.security.models.UserAccount;
import com.horeca.site.services.HotelService;
import com.horeca.site.services.services.StayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

import static com.horeca.utils.UrlPartExtractors.*;

@Service
@Transactional
public class AccessChecker {

    @Autowired
    private StayService stayService;

    @Autowired
    private HotelService hotelService;

    public boolean checkForHotel(Authentication authentication, HttpServletRequest request) {
        String servletPath = request.getServletPath();
        Long hotelId = extractIdFromServletPath(servletPath, hotelIdPattern);
        return checkForHotelHelper(authentication, hotelId);
    }

    public boolean checkForStayFromCollection(Authentication authentication, StayView filterObject) {
        return checkForStayHelper(authentication, filterObject);
    }

    public boolean checkForStay(Authentication authentication, HttpServletRequest request) {
        String servletPath = request.getServletPath();
        String pin = extractStayPinFromServletPath(servletPath, stayPinPattern);
        return checkForStayHelper(authentication, pin);
    }

    public boolean checkForStayCheckIn(Authentication authentication, HttpServletRequest request) {
        String servletPath = request.getServletPath();
        String pin = extractStayPinFromServletPath(servletPath, checkInPattern);
        return checkForStayHelper(authentication, pin);
    }

    public boolean checkForStayCheckOut(Authentication authentication, HttpServletRequest request) {
        String servletPath = request.getServletPath();
        String pin = extractStayPinFromServletPath(servletPath, checkOutPattern);
        return checkForStayHelper(authentication, pin);
    }

    public boolean checkAddingStay(Authentication authentication, StayPOST stayPOST) {
        Long requestedHotelId = stayPOST.getHotelId();
        Long requestedGuestId = stayPOST.getGuestId();
        boolean allowedToManageHotel = checkForHotelHelper(authentication, requestedHotelId);
        boolean allowedToManageGuest = checkForGuestHelper(authentication, requestedGuestId);
        return (allowedToManageHotel && allowedToManageGuest);
    }

    public boolean checkForUserAccountFromCollection(Authentication authentication, UserAccount filterObject) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserAccount) {
            UserAccount userAccount = (UserAccount) principal;
            if (userAccount.getHotelId().equals(filterObject.getHotelId()))
                return true;
        }

        return false;
    }

    private boolean checkForStayHelper(Authentication authentication, String pin) {
        Stay stay = stayService.getWithoutCheckingStatus(pin);
        StayView stayView = stay.toView();
        return checkForStayHelper(authentication, stayView);
    }

    private boolean checkForStayHelper(Authentication authentication, StayView stay) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof GuestAccount) {
            GuestAccount guestAccount = (GuestAccount) principal;
            if (stay.getPin().equals(guestAccount.getPin()))
                return true;
        }
        else if (principal instanceof UserAccount) {
            UserAccount userAccount = (UserAccount) principal;
            Long requestedId = stay.getHotel().getId();
            if (userAccount.getHotelId().equals(requestedId))
                return true;
        }

        return false;
    }

    private boolean checkForHotelHelper(Authentication authentication, Long hotelId) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof SalesmanAccount) {
            // allow any salesman to update hotels' data
            return true;
        }
        else if (principal instanceof UserAccount) {
            if (hotelId != null) {
                UserAccount userAccount = (UserAccount) principal;
                if (hotelId.equals(userAccount.getHotelId()))
                    return true;
            }
        }
        return false;
    }

    private boolean checkForGuestHelper(Authentication authentication, Long guestId) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserAccount) {
            UserAccount userAccount = (UserAccount) principal;
            Hotel hotel = hotelService.get(userAccount.getHotelId());
            Set<Guest> hotelGuests = hotel.getGuests();
            for (Guest hotelGuest : hotelGuests) {
                if (hotelGuest.getId().equals(guestId))
                    return true;
            }
        }
        return false;
    }
}
