package com.horeca.site.repositories.services;

import com.horeca.site.models.hotel.services.petcare.PetCareItem;
import org.springframework.data.repository.CrudRepository;

public interface PetCareItemRepository extends CrudRepository<PetCareItem, Long> {
}
