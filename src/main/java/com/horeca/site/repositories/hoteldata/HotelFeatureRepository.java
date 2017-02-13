package com.horeca.site.repositories.hoteldata;

import com.horeca.site.models.hoteldata.HotelFeature;
import org.springframework.data.repository.CrudRepository;

public interface HotelFeatureRepository extends CrudRepository<HotelFeature, Long> {
    HotelFeature findByName(String name);
}
