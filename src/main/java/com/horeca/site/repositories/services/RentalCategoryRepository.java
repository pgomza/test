package com.horeca.site.repositories.services;

import com.horeca.site.models.hotel.services.rental.RentalCategory;
import org.springframework.data.repository.CrudRepository;

public interface RentalCategoryRepository extends CrudRepository<RentalCategory, Long> {
}
