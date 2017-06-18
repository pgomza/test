package com.horeca.site.controllers.services;

import com.horeca.site.models.Language;
import com.horeca.site.models.hotel.services.restaurantmenu.RestaurantMenuItem;
import com.horeca.site.models.hotel.services.restaurantmenu.RestaurantMenuItemTranslation;
import com.horeca.site.models.hotel.services.restaurantmenu.RestaurantMenuItemTranslationUpdate;
import com.horeca.site.services.RestaurantMenuTranslationService;
import com.horeca.site.services.services.RestaurantMenuService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Api(value = "hotels")
@RestController
@RequestMapping("/api/hotels")
public class RestaurantMenuTranslationController {

    @Autowired
    private RestaurantMenuService service;

    @Autowired
    private RestaurantMenuTranslationService translationService;

    @RequestMapping(value = "/{hotelId}/services/restaurantmenu/categories/{categoryId}/items/{itemId}/translations",
            method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public RestaurantMenuItemTranslation addItemTranslation(@PathVariable("hotelId") Long hotelId,
                                                            @PathVariable("categoryId") Long categoryId,
                                                            @PathVariable("itemId") Long itemId,
                                                            @RequestBody RestaurantMenuItemTranslationUpdate itemTranslation) {

        RestaurantMenuItem item = service.getItem(hotelId, categoryId, itemId);
        return translationService.addItemTranslation(item, Language.PL, itemTranslation);
    }

    @RequestMapping(value = "/{hotelId}/services/restaurantmenu/categories/{categoryId}/items/{itemId}/translations/{translationId}",
            method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public RestaurantMenuItemTranslation addItemTranslation(@PathVariable("hotelId") Long hotelId,
                                                            @PathVariable("categoryId") Long categoryId,
                                                            @PathVariable("itemId") Long itemId,
                                                            @PathVariable("translationId") Long translationId,
                                                            @RequestBody RestaurantMenuItemTranslationUpdate itemTranslation) {

        service.getItem(hotelId, categoryId, itemId);
        return translationService.updateItemTranslation(translationId, itemTranslation);
    }
}
