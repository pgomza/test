package com.horeca.site.repositories.services;

import com.horeca.site.models.hotel.services.restaurantmenu.RestaurantMenuItem;
import org.springframework.data.repository.CrudRepository;

public interface RestaurantMenuItemRepository extends CrudRepository<RestaurantMenuItem, Long> {
}
