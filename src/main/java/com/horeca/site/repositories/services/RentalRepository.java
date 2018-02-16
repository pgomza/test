package com.horeca.site.repositories.services;

import com.horeca.site.models.hotel.services.rental.Rental;
import org.springframework.data.repository.CrudRepository;

public interface RentalRepository extends CrudRepository<Rental, Long> {
}
