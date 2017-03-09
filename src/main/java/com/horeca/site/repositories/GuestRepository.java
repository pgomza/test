package com.horeca.site.repositories;

import com.horeca.site.models.guest.Guest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GuestRepository extends CrudRepository<Guest, Long> {

    @Query("select g from Guest g, Stay s where s.guest.id = :guestId and s.hotel.id = :hotelId")
    List<Guest> checkIfGuestInHotel(@Param("guestId") Long guestId, @Param("hotelId") Long hotelId);
}
