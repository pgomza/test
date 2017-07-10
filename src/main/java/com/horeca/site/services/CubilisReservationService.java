package com.horeca.site.services;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.cubilis.CubilisCustomer;
import com.horeca.site.models.cubilis.CubilisReservation;
import com.horeca.site.models.guest.Guest;
import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.repositories.CubilisReservationRepository;
import com.horeca.site.services.services.StayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class CubilisReservationService {

    @Autowired
    private GuestService guestService;

    @Autowired
    private StayService stayService;

    @Autowired
    private CubilisReservationRepository repository;

    public List<CubilisReservation> getAll(Long hotelId) {
        return repository.getByHotelId(hotelId);
    }

    public List<CubilisReservation> getAllNotRejected(Long hotelId) {
        return getAll(hotelId).stream().filter(r -> !r.isRejected()).collect(Collectors.toList());
    }

    public CubilisReservation get(Long hotelId, Long id) {
        CubilisReservation found = repository.getByHotelIdAndId(hotelId, id);
        if (found == null) {
            throw new ResourceNotFoundException();
        }
        return found;
    }

    public void confirm(Long hotelId, List<Long> reservationIds) {
        List<CubilisReservation> reservations = getByIds(hotelId, reservationIds);
        merge(reservations);
    }

    public void reject(Long hotelId, List<Long> reservationIds) {
        List<CubilisReservation> reservations = getByIds(hotelId, reservationIds);
        reservations.forEach(r -> r.setRejected(true));
        save(reservations);
    }

    void merge(List<CubilisReservation> reservations) {
        for (CubilisReservation reservation : reservations) {
            Hotel hotel = reservation.getHotel();

            CubilisCustomer customer = reservation.getCustomer();
            Guest matchingGuest = getGuestForCustomer(hotel.getId(), customer);

            Stay stay = new Stay();
            stay.setCubilisId(reservation.getId());
            stay.setFromDate(reservation.getArrival().toLocalDate());
            stay.setToDate(reservation.getDeparture());
            stay.setRoomNumber("");

            stay.setGuest(matchingGuest);
            stay.setHotel(hotel);

            stayService.registerNewStay(stay);
        }
    }

    void save(List<CubilisReservation> reservations) {
        repository.save(reservations);
    }

    private List<CubilisReservation> getByIds(Long hotelId, List<Long> reservationIds) {
        return reservationIds.stream().map(id -> get(hotelId, id)).collect(Collectors.toList());
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
