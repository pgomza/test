package com.horeca.site.repositories.services;

import com.horeca.site.models.hotel.services.petcare.PetCare;
import org.springframework.data.repository.CrudRepository;

public interface PetCareRepository extends CrudRepository<PetCare, Long> {
}
