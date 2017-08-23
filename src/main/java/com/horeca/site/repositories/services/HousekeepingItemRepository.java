package com.horeca.site.repositories.services;

import com.horeca.site.models.hotel.services.housekeeping.HousekeepingItem;
import org.springframework.data.repository.CrudRepository;

public interface HousekeepingItemRepository extends CrudRepository<HousekeepingItem, Long> {
}
