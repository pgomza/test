package com.horeca.site.repositories.cubilis;

import com.horeca.site.models.cubilis.CubilisReservation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CubilisReservationRepository extends CrudRepository<CubilisReservation, Long> {

    @Query("select r from CubilisReservation r where r.hotel.id = :hotelId")
    List<CubilisReservation> getByHotelId(@Param("hotelId") Long hotelId);

    @Query("select r from CubilisReservation r where r.hotel.id = :hotelId and r.id = :id")
    CubilisReservation getByHotelIdAndId(@Param("hotelId") Long hotelId, @Param("id") Long id);
}
