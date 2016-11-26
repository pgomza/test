package com.horeca.site.repositories.services;

import com.horeca.site.models.hotel.services.petcare.calendar.PetCareCalendarHour;
import org.springframework.data.repository.CrudRepository;

public interface PetCareCalendarHourRepository extends CrudRepository<PetCareCalendarHour, Long> {
}
