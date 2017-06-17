package com.horeca.site.repositories.services;

import com.horeca.site.models.hotel.services.restaurantmenu.RestaurantMenu;
import org.springframework.data.repository.CrudRepository;

public interface RestaurantMenuRepository extends CrudRepository<RestaurantMenu, Long>{
}
