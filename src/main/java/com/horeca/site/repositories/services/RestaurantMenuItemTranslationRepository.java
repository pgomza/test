package com.horeca.site.repositories.services;

import com.horeca.site.models.Language;
import com.horeca.site.models.hotel.services.restaurantmenu.RestaurantMenuItemTranslation;
import org.springframework.data.repository.CrudRepository;

public interface RestaurantMenuItemTranslationRepository extends CrudRepository<RestaurantMenuItemTranslation, Long> {

    RestaurantMenuItemTranslation findByTranslatableIdAndLanguage(Long translatableId, Language language);
}
