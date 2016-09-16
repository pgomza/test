package com.horeca.site.repositories;

import org.springframework.data.repository.CrudRepository;

import com.horeca.site.models.hotel.Hotel;

public interface HotelRepository extends CrudRepository<Hotel, Long>{
}
