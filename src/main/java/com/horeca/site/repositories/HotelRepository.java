package com.horeca.site.repositories;

import com.horeca.site.models.hotel.Hotel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HotelRepository extends PagingAndSortingRepository<Hotel, Long> {

    @Query("select count(*) from Hotel")
    Long getTotalCount();

    @Query("select h.id from Hotel h where lower(h.name) like %:name%")
    List<Long> getIdsByName(@Param("name") String name);

    @Query("select h.id as id, h.address as address from Hotel h where lower(h.address) like %:city%")
    List<Object[]> getCandidatesByCity(@Param("city") String city);
}
