package com.horeca.site.repositories.services;

import com.horeca.site.models.hotel.services.restaurantmenu.RestaurantMenuCategory;
import org.springframework.data.repository.CrudRepository;

public interface RestaurantMenuCategoryRepository extends CrudRepository<RestaurantMenuCategory, Long> {
}
