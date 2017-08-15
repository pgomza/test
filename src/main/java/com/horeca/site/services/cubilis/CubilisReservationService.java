package com.horeca.site.services.cubilis;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.cubilis.CubilisCustomer;
import com.horeca.site.models.cubilis.CubilisReservation;
import com.horeca.site.models.cubilis.CubilisReservationUpdate;
import com.horeca.site.models.guest.Guest;
import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.repositories.cubilis.CubilisReservationRepository;
import com.horeca.site.services.GuestService;
import com.horeca.site.services.services.StayService;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class CubilisReservationService {

    private static final int DAYS_TO_INVALIDATE_RESERVATION = 7;

    @Autowired
    private GuestService guestService;

    @Autowired
    private StayService stayService;

    @Autowired
    private CubilisReservationRepository repository;

    List<CubilisReservation> getAll(Long hotelId) {
        return repository.getByHotelId(hotelId);
    }

    private List<CubilisReservation> getAllNotRejected(Long hotelId) {
        return getAll(hotelId).stream().filter(r -> !r.isRejected()).collect(Collectors.toList());
    }

    public List<CubilisReservationUpdate> getAllNotRejectedViews(Long hotelId) {
        return getAllNotRejected(hotelId).stream()
                .map(CubilisReservation::toView)
                .collect(Collectors.toList());
    }

    private CubilisReservation get(Long hotelId, Long id) {
        CubilisReservation found = repository.getByHotelIdAndId(hotelId, id);
        if (found == null) {
            throw new ResourceNotFoundException();
        }
        return found;
    }

    public CubilisReservationUpdate update(Long hotelId, Long id, CubilisReservationUpdate updated) {
        CubilisReservation current = get(hotelId, id);

        Guest matchingGuest = null;
        if (updated.getGuestId() != null) {
            matchingGuest = guestService.get(hotelId, updated.getGuestId());
        }
        current.setGuest(matchingGuest);

        current.setArrival(updated.getArrival().toLocalDateTime(LocalTime.MIDNIGHT));
        current.setDeparture(updated.getDeparture());
        current.setCustomer(updated.getCubilisCustomer());

        CubilisReservation saved = repository.save(current);
        return saved.toView();
    }

    public void confirm(Long hotelId, List<Long> reservationIds) {
        List<CubilisReservation> reservations = getByIds(hotelId, reservationIds);
        for (CubilisReservation reservation : reservations) {
            if (reservation.isRejected()) {
                throw new ResourceNotFoundException();
            }
        }
        merge(reservations);
        repository.delete(reservations);
    }

    public void reject(Long hotelId, List<Long> reservationIds) {
        List<CubilisReservation> reservations = getByIds(hotelId, reservationIds);
        reservations.forEach(r -> r.setRejected(true));
        save(reservations);
    }

    public void deleteOutdated() {
        for (CubilisReservation reservation : repository.findAll()) {
            if (reservation.isRejected() && isOutdated(reservation)) {
                repository.delete(reservation);
            }
        }
    }

    void merge(List<CubilisReservation> reservations) {
        for (CubilisReservation reservation : reservations) {
            Hotel hotel = reservation.getHotel();

            // the guest that has been specified manually takes precedence
            Guest matchingGuest = reservation.getGuest();
            if (matchingGuest == null) {
                CubilisCustomer customer = reservation.getCustomer();
                matchingGuest = getGuestForCustomer(hotel.getId(), customer);
            }

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

    private static boolean isOutdated(CubilisReservation reservation) {
        LocalDate todayMinusDaysToInvalidation = LocalDate.now().minusDays(DAYS_TO_INVALIDATE_RESERVATION);
        if (reservation.getDeparture().isBefore(todayMinusDaysToInvalidation)) {
            return true;
        }
        return false;
    }
}
