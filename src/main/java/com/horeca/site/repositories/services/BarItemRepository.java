package com.horeca.site.repositories.services;

import com.horeca.site.models.hotel.services.bar.BarItem;
import org.springframework.data.repository.CrudRepository;

public interface BarItemRepository extends CrudRepository<BarItem, Long> {
}
