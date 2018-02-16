package com.horeca.site.repositories.services;

import com.horeca.site.models.hotel.services.carpark.CarPark;
import org.springframework.data.repository.CrudRepository;

public interface CarParkRepository extends CrudRepository<CarPark, Long> {
}
