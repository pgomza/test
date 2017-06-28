package com.horeca.site.repositories.services;

import com.horeca.site.models.hotel.services.rental.RentalItem;
import org.springframework.data.repository.CrudRepository;

public interface RentalItemRepository extends CrudRepository<RentalItem, Long> {
}
