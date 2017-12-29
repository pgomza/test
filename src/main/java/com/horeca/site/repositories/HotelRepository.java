package com.horeca.site.repositories;

import com.horeca.site.models.Currency;
import com.horeca.site.models.hotel.Hotel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HotelRepository extends PagingAndSortingRepository<Hotel, Long> {

    @Query("select count(*) from Hotel h where h.isMarkedAsDeleted = 0")
    Long getTotalCount();

    @Query("select h.id from Hotel h where h.isMarkedAsDeleted = 0")
    List<Long> getAll();

    @Query("select h.id from Hotel h where lower(h.name) like %:name% and h.isMarkedAsDeleted = 0 and " +
            "h.isTestHotel = :withTestHotels")
    List<Long> getIdsByName(@Param("name") String name, @Param("withTestHotels") Boolean withTestHotels);

    @Query("select h.id as id, h.address as address from Hotel h where lower(h.address) like %:city% and " +
            "h.isMarkedAsDeleted = 0 and h.isTestHotel = :withTestHotels")
    List<Object[]> getCandidatesByCity(@Param("city") String city, @Param("withTestHotels") Boolean withTestHotels);

    @Query("select h.id from Hotel h where h.isMarkedAsDeleted = 0 and h.cubilisSettings.isEnabled = 1 and " +
            "h.cubilisConnectionStatus.status like 'SUCCESS'")
    List<Long> getIdsOfCubilisEligible();

    @Query("select h.id from Hotel h where h.isMarkedAsDeleted = 1")
    List<Long> getAllMarkedAsDeleted();

    @Query("select h.currency from Hotel h where h.id = :id")
    Currency getCurrency(@Param("id") Long id);
}
