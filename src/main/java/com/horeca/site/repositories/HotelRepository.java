package com.horeca.site.repositories;

import com.horeca.site.models.hotel.Hotel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HotelRepository extends PagingAndSortingRepository<Hotel, Long> {

    @Query("select count(*) from Hotel")
    Long getTotalCount();

    @Query("select h from Hotel h where lower(h.address) like %:city%)")
    List<Hotel> getByCity(@Param("city") String city);

    @Query("select h from Hotel h where lower(h.name) like %:name%")
    List<Hotel> getByName(@Param("name") String name);
}
