package com.horeca.site.services;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.guest.Guest;
import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.repositories.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class GuestService {

    @Autowired
    private GuestRepository repository;

    @Autowired
    private HotelService hotelService;

    public Set<Guest> getAll(Long hotelId) {
        Hotel hotel = hotelService.get(hotelId);
        return hotel.getGuests();
    }

    public Guest get(Long hotelId, Long id) {
        Set<Guest> guests = getAll(hotelId);
        Optional<Guest> guestOptional = guests.stream().filter(g -> id.equals(g.getId())).findAny();
        if (guestOptional.isPresent()) {
            return guestOptional.get();
        }
        else {
            throw new ResourceNotFoundException("Could not find a guest with such an id");
        }
    }

    public Guest save(Long hotelId, Guest entity) {
        Hotel hotel = hotelService.get(hotelId);
        Guest saved = repository.save(entity);
        Set<Guest> currentGuests = hotel.getGuests();
        if (currentGuests == null) {
            currentGuests = new HashSet<>();
        }
        if (!currentGuests.contains(saved)) {
            currentGuests.add(saved);
            hotelService.update(hotelId, hotel);
        }
        return saved;
    }

    public void delete(Long hotelId, Long id) {
        hotelService.ensureExists(hotelId);
        repository.delete(id);
    }
}
