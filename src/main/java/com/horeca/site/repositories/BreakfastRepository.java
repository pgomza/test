package com.horeca.site.repositories;

import com.horeca.site.models.hotel.services.breakfast.Breakfast;
import org.springframework.data.repository.CrudRepository;

public interface BreakfastRepository extends CrudRepository<Breakfast, Long> {
}
