package com.horeca.site.repositories.services;

import com.horeca.site.models.stay.Stay;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface StayRepository extends CrudRepository<Stay, String> {

    @Query("select s.pin from Stay s where s.hotel.id = :hotelId")
    Collection<String> findByHotelId(@Param("hotelId") Long hotelId);

    @Query("select s.hotel.id from Stay s where s.pin = :pin")
    Long getHotelIdOfStay(@Param("pin") String pin);
}
