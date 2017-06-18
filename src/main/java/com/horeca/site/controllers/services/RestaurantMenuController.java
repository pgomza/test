package com.horeca.site.controllers.services;

import com.horeca.site.models.Language;
import com.horeca.site.models.hotel.services.restaurantmenu.RestaurantMenu;
import com.horeca.site.models.hotel.services.restaurantmenu.RestaurantMenuCategory;
import com.horeca.site.models.hotel.services.restaurantmenu.RestaurantMenuItem;
import com.horeca.site.services.RestaurantMenuTranslationService;
import com.horeca.site.services.services.RestaurantMenuService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Api(value = "hotels")
@RestController
@RequestMapping("/api/hotels")
public class RestaurantMenuController {

    @Autowired
    private RestaurantMenuService service;

    @Autowired
    private RestaurantMenuTranslationService translationService;

    @RequestMapping(value = "/{hotelId}/services/restaurantmenu", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RestaurantMenu get(@PathVariable("hotelId") Long hotelId) {
        return service.get(hotelId);
    }

    @RequestMapping(value = "/{hotelId}/services/restaurantmenu", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RestaurantMenu add(@PathVariable("hotelId") Long hotelId) {
        return service.addDefaultRestaurantMenu(hotelId);
    }

    @RequestMapping(value = "/{hotelId}/services/restaurantmenu/categories", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RestaurantMenuCategory addCategory(@PathVariable("hotelId") Long hotelId, @RequestBody RestaurantMenuCategory category) {
        return service.addCategory(hotelId, category);
    }

    @RequestMapping(value = "/{hotelId}/services/restaurantmenu/categories/{categoryId}/items",
            method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public RestaurantMenuItem addItem(@PathVariable("hotelId") Long hotelId,
                                      @PathVariable("categoryId") Long categoryId,
                                      @RequestBody RestaurantMenuItem item) {

        return service.addItem(hotelId, categoryId, item);
    }

    @RequestMapping(value = "/{hotelId}/services/restaurantmenu/categories/{categoryId}/items/{itemId}",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public RestaurantMenuItem getItem(@PathVariable("hotelId") Long hotelId,
                                  @PathVariable("categoryId") Long categoryId,
                                  @PathVariable("itemId") Long itemId) {

        RestaurantMenuItem item = service.getItem(hotelId, categoryId, itemId);
        return translationService.translateItem(item, Language.PL);
    }
}
