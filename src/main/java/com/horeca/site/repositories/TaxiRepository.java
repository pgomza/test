package com.horeca.site.repositories;

import com.horeca.site.models.hotel.services.taxi.Taxi;
import org.springframework.data.repository.CrudRepository;

public interface TaxiRepository extends CrudRepository<Taxi, Long> {
}
