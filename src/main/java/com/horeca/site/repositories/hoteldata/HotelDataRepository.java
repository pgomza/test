package com.horeca.site.repositories.hoteldata;

import com.horeca.site.models.hoteldata.HotelData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HotelDataRepository extends PagingAndSortingRepository<HotelData, Long> {

    @Query("select count(*) from HotelData")
    Long getTotalCount();

    @Query("select h from HotelData h where lower(h.address) like %:city%)")
    List<HotelData> getByCity(@Param("city") String city);

    @Query("select h from HotelData h where lower(h.name) like %:name%")
    List<HotelData> getByName(@Param("name") String name);
}
