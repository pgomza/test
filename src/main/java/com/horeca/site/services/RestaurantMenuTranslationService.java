package com.horeca.site.services;

import com.horeca.site.models.Language;
import com.horeca.site.models.hotel.services.restaurantmenu.RestaurantMenuItem;
import com.horeca.site.models.hotel.services.restaurantmenu.RestaurantMenuItemTranslation;
import com.horeca.site.models.hotel.services.restaurantmenu.RestaurantMenuItemTranslationUpdate;
import com.horeca.site.repositories.services.RestaurantMenuItemTranslationRepository;
import com.horeca.site.services.services.RestaurantMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RestaurantMenuTranslationService {

    @Autowired
    private RestaurantMenuService service;

    @Autowired
    private RestaurantMenuItemTranslationRepository itemTranslationRepository;

    public RestaurantMenuItem translateItem(RestaurantMenuItem item, Language language) {
        RestaurantMenuItemTranslation translation =
                itemTranslationRepository.findByTranslatableIdAndLanguage(item.getId(), language);
        return (translation != null) ? translation.translate() : item;
    }

    public RestaurantMenuItemTranslation getItemTranslation(Long translationId) {
        return itemTranslationRepository.findOne(translationId);
    }

    public RestaurantMenuItemTranslation addItemTranslation(RestaurantMenuItem item, Language language,
                                                 RestaurantMenuItemTranslationUpdate translation) {
        RestaurantMenuItemTranslation toSave = new RestaurantMenuItemTranslation();
        toSave.setTranslatable(item);
        toSave.setName(translation.getName());
        toSave.setDescription(translation.getDescription());
        toSave.setLanguage(language);
        return itemTranslationRepository.save(toSave);
    }

    public RestaurantMenuItemTranslation updateItemTranslation(Long translationId,
                                                               RestaurantMenuItemTranslationUpdate updated) {
        RestaurantMenuItemTranslation translation = getItemTranslation(translationId);
        translation.setDescription(updated.getDescription());
        translation.setName(updated.getName());
        return itemTranslationRepository.save(translation);
    }

    public void deleteItemTranslation(Long translationId) {
        itemTranslationRepository.delete(translationId);
    }
}
