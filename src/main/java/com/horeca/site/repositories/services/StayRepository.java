package com.horeca.site.repositories.services;

import com.horeca.site.models.stay.Stay;
import org.springframework.data.repository.CrudRepository;

public interface StayRepository extends CrudRepository<Stay, String> {
}
