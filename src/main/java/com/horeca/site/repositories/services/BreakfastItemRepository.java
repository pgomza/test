package com.horeca.site.repositories.services;

import com.horeca.site.models.hotel.services.breakfast.BreakfastItem;
import org.springframework.data.repository.CrudRepository;

public interface BreakfastItemRepository extends CrudRepository<BreakfastItem, Long> {
}
