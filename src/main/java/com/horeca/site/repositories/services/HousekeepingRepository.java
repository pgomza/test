package com.horeca.site.repositories.services;

import com.horeca.site.models.hotel.services.housekeeping.Housekeeping;
import org.springframework.data.repository.CrudRepository;

public interface HousekeepingRepository extends CrudRepository<Housekeeping, Long> {
}
