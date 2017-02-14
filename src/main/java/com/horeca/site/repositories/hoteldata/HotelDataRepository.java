package com.horeca.site.repositories.hoteldata;

import com.horeca.site.models.hoteldata.HotelData;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface HotelDataRepository extends PagingAndSortingRepository<HotelData, Long> {
}
