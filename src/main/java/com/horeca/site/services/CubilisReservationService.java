package com.horeca.site.services;

import com.horeca.site.models.cubilis.CubilisCustomer;
import com.horeca.site.models.cubilis.CubilisReservation;
import com.horeca.site.models.guest.Guest;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.services.services.StayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CubilisReservationService {

    @Autowired
    private GuestService guestService;

    @Autowired
    private StayService stayService;

    public void mergeReservations(Long hotelId, List<CubilisReservation> reservations) {
        for (CubilisReservation reservation : reservations) {
            CubilisCustomer customer = reservation.getCustomer();
            Guest matchingGuest = getGuestForCustomer(hotelId, customer);

            Stay stay = new Stay();
            stay.setCubilisId(reservation.getId());
            stay.setFromDate(reservation.getArrival().toLocalDate());
            stay.setToDate(reservation.getDeparture());
            stay.setGuest(matchingGuest);
            stay.setRoomNumber("");

            stayService.registerNewStay(stay);
        }
    }

    private Guest getGuestForCustomer(Long hotelId, CubilisCustomer customer) {
        Set<Guest> hotelGuests = guestService.getAll(hotelId);

        Guest matchingGuest = null;
        for (Guest guest : hotelGuests) {
            if (    guest.getEmail() != null && guest.getEmail().equalsIgnoreCase(customer.getEmail()) &&
                    guest.getLastName().equalsIgnoreCase(customer.getLastName()) &&
                    guest.getFirstName().equalsIgnoreCase(customer.getFirstName())) {

                matchingGuest = guest;
                break;
            }
        }

        if (matchingGuest == null) { // create a new guest
            Guest guest = new Guest();
            guest.setFirstName(customer.getFirstName());
            guest.setLastName(customer.getLastName());
            guest.setEmail(customer.getEmail());
            guest.setNationality("GB");
            matchingGuest = guestService.save(hotelId, guest);
        }

        return matchingGuest;
    }
}
