package com.horeca.site.repositories;

import com.horeca.site.models.guest.Guest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface GuestRepository extends CrudRepository<Guest, Long> {

    @Query("select case when count(g) > 0 then true else false end " +
            "from Guest g, Stay s where s.guest.id = :guestId and s.hotel.id = :hotelId")
    boolean checkIfGuestInHotel(@Param("guestId") Long guestId, @Param("hotelId") Long hotelId);

    @Query("select case when count(s) > 0 then true else false end " +
            "from Stay s where s.guest.id = :guestId")
    boolean checkIfGuestInAnyStay(@Param("guestId") Long guestId);
}
