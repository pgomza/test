package com.horeca.site.repositories;

import com.horeca.site.models.hotel.services.spa.calendar.SpaCalendarHour;
import org.springframework.data.repository.CrudRepository;

public interface SpaCalendarHourRepository extends CrudRepository<SpaCalendarHour, Long> {
}
