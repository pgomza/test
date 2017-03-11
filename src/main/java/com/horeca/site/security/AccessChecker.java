package com.horeca.site.security;

import com.horeca.site.models.guest.Guest;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.models.stay.StayPOST;
import com.horeca.site.models.stay.StayView;
import com.horeca.site.repositories.GuestRepository;
import com.horeca.site.security.models.GuestAccount;
import com.horeca.site.security.models.UserAccount;
import com.horeca.site.services.services.StayService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AccessChecker {

    private final Logger logger = Logger.getLogger(AccessChecker.class);
    private final Pattern hotelIdPattern = Pattern.compile("/api/hotels/(\\d+)");
    private final Pattern stayPinPattern = Pattern.compile("/api/stays/([a-z0-9]+)");
    private final Pattern checkInPattern = Pattern.compile("/api/check-in/([a-z0-9]+)");
    private final Pattern checkOutPattern = Pattern.compile("/api/check-out/([a-z0-9]+)");
    private final Pattern guestIdPattern = Pattern.compile("/api/guests/(\\d+)");

    @Autowired
    private StayService stayService;

    @Autowired
    private GuestRepository guestRepository;

    public boolean checkForHotel(Authentication authentication, HttpServletRequest request) {
        String servletPath = request.getServletPath();
        Long hotelId = extractIdFromServletPath(servletPath, hotelIdPattern);
        return checkForHotelHelper(authentication, hotelId);
    }

    public boolean checkForStayFromCollection(Authentication authentication, StayView filterObject) {
        return checkForStayHelper(authentication, filterObject.getPin());
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

    public boolean checkForGuestFromCollection(Authentication authentication, Guest filterObject) {
        return checkForGuestHelper(authentication, filterObject.getId());
    }

    public boolean checkForGuest(Authentication authentication, HttpServletRequest request) {
        String servletPath = request.getServletPath();
        Long hotelId = extractIdFromServletPath(servletPath, guestIdPattern);
        return checkForGuestHelper(authentication, hotelId);
    }

    private boolean checkForStayHelper(Authentication authentication, String pin) {
        if (pin != null) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof GuestAccount) {
                GuestAccount guestAccount = (GuestAccount) principal;
                if (pin.equals(guestAccount.getPin()))
                    return true;
            }
            else if (principal instanceof UserAccount) {
                UserAccount userAccount = (UserAccount) principal;
                Stay stay = stayService.getWithoutChecks(pin);
                Long requestedId = stay.getHotel().getId();
                if (userAccount.getHotelId().equals(requestedId))
                    return true;
            }
        }
        return false;
    }

    private boolean checkForGuestHelper(Authentication authentication, Long guestId) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserAccount) {
            UserAccount userAccount = (UserAccount) principal;
            boolean guestInTheHotel = guestRepository.checkIfGuestInHotel(guestId, userAccount.getHotelId());
            if (guestInTheHotel)
                return true;
            else {
                // maybe the currently filtered guest hasn't been associated with any stay yet?
                // if so, they should be available too
                return !guestRepository.checkIfGuestInAnyStay(guestId);
            }
        }
        return false;
    }

    private boolean checkForHotelHelper(Authentication authentication, Long hotelId) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserAccount) {
            if (hotelId != null) {
                UserAccount userAccount = (UserAccount) principal;
                if (hotelId.equals(userAccount.getHotelId()))
                    return true;
            }
        }
        return false;
    }

    private Long extractIdFromServletPath(String servletPath, Pattern pattern) {
        Matcher matcher = pattern.matcher(servletPath);
        Long result = null;
        if (matcher.find() && matcher.groupCount() == 1) {
            try {
                result = Long.valueOf(matcher.group(1));
            }
            catch (NumberFormatException ex) {
                logger.warn("Could not extract an id from the request path: " + servletPath);
                throw ex;
            }
        }
        return result;
    }

    private String extractStayPinFromServletPath(String servletPath, Pattern pattern) {
        Matcher matcher = pattern.matcher(servletPath);
        String result = null;
        if (matcher.find() && matcher.groupCount() == 1) {
            try {
                result = matcher.group(1);
            }
            catch (NumberFormatException ex) {
                logger.warn("Could not extract a stay pin from the request path: " + servletPath);
                throw ex;
            }
        }
        return result;
    }
}
