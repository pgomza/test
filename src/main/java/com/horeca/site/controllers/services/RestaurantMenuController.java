package com.horeca.site.controllers.services;

import com.horeca.site.handlers.HotelId;
import com.horeca.site.handlers.TranslateReturnValue;
import com.horeca.site.models.hotel.services.restaurantmenu.*;
import com.horeca.site.services.services.RestaurantMenuService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "hotels")
@RestController
@RequestMapping("/api/hotels")
public class RestaurantMenuController {

    @Autowired
    private RestaurantMenuService service;

    @TranslateReturnValue
    @RequestMapping(value = "/{hotelId}/services/restaurantmenu", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RestaurantMenu get(@HotelId @PathVariable("hotelId") Long hotelId) {
        return service.get(hotelId);
    }

    @RequestMapping(value = "/{hotelId}/services/restaurantmenu", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RestaurantMenu update(@PathVariable("hotelId") Long hotelId, @RequestBody RestaurantMenu menu) {
        return service.update(hotelId, menu);
    }

    @RequestMapping(value = "/{hotelId}/services/restaurantmenu", method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RestaurantMenu update(@PathVariable("hotelId") Long hotelId, @RequestBody RestaurantMenuPATCH patch) {
        return service.patch(hotelId, patch);
    }

    @RequestMapping(value = "/{hotelId}/services/restaurantmenu", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RestaurantMenu add(@PathVariable("hotelId") Long hotelId) {
        return service.addDefaultRestaurantMenu(hotelId);
    }

    @TranslateReturnValue
    @RequestMapping(value = "/{hotelId}/services/restaurantmenu/categories", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RestaurantMenuCategory> getCategories(@HotelId @PathVariable("hotelId") Long hotelId) {
        return service.getCategories(hotelId);
    }

    @RequestMapping(value = "/{hotelId}/services/restaurantmenu/categories", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RestaurantMenuCategory addCategory(@PathVariable("hotelId") Long hotelId, @RequestBody RestaurantMenuCategory category) {
        return service.addCategory(hotelId, category);
    }

    @TranslateReturnValue
    @RequestMapping(value = "/{hotelId}/services/restaurantmenu/categories/{categoryId}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RestaurantMenuCategory getCategory(@HotelId @PathVariable("hotelId") Long hotelId,
                                                @PathVariable("categoryId") Long categoryId) {
        return service.getCategory(hotelId, categoryId);
    }

    @RequestMapping(value = "/{hotelId}/services/restaurantmenu/categories/{categoryId}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RestaurantMenuCategory updateCategory(@PathVariable("hotelId") Long hotelId,
                                                 @PathVariable("categoryId") Long categoryId,
                                                 @RequestBody RestaurantMenuCategory category) {
        return service.updateCategory(hotelId, categoryId, category);
    }

    @RequestMapping(value = "/{hotelId}/services/restaurantmenu/categories/{categoryId}", method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RestaurantMenuCategory patchCategory(@PathVariable("hotelId") Long hotelId,
                                                 @PathVariable("categoryId") Long categoryId,
                                                 @RequestBody RestaurantMenuCategoryPATCH patch) {
        return service.patchCategory(hotelId, categoryId, patch);
    }

    @RequestMapping(value = "/{hotelId}/services/restaurantmenu/categories/{categoryId}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteCategory(@PathVariable("hotelId") Long hotelId,
                                                 @PathVariable("categoryId") Long categoryId) {
        service.deleteCategory(hotelId, categoryId);
    }

    @TranslateReturnValue
    @RequestMapping(value = "/{hotelId}/services/restaurantmenu/categories/{categoryId}/items",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RestaurantMenuItem> getItems(@HotelId @PathVariable("hotelId") Long hotelId,
                                            @PathVariable("categoryId") Long categoryId) {

        return service.getItems(hotelId, categoryId);
    }

    @RequestMapping(value = "/{hotelId}/services/restaurantmenu/categories/{categoryId}/items",
            method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public RestaurantMenuItem addItem(@PathVariable("hotelId") Long hotelId,
                                      @PathVariable("categoryId") Long categoryId,
                                      @RequestBody RestaurantMenuItem item) {

        return service.addItem(hotelId, categoryId, item);
    }

    @TranslateReturnValue
    @RequestMapping(value = "/{hotelId}/services/restaurantmenu/categories/{categoryId}/items/{itemId}",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public RestaurantMenuItem getItem(@HotelId @PathVariable("hotelId") Long hotelId,
                                  @PathVariable("categoryId") Long categoryId,
                                  @PathVariable("itemId") Long itemId) {

        return service.getItem(hotelId, categoryId, itemId);
    }

    @RequestMapping(value = "/{hotelId}/services/restaurantmenu/categories/{categoryId}/items/{itemId}",
            method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public RestaurantMenuItem updateItem(@PathVariable("hotelId") Long hotelId,
                                         @PathVariable("categoryId") Long categoryId,
                                         @PathVariable("itemId") Long itemId,
                                         @RequestBody RestaurantMenuItem item) {

        item.setId(itemId);
        return service.updateItem(hotelId, categoryId, item);
    }

    @RequestMapping(value = "/{hotelId}/services/restaurantmenu/categories/{categoryId}/items/{itemId}",
            method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteItem(@PathVariable("hotelId") Long hotelId,
                                         @PathVariable("categoryId") Long categoryId,
                                         @PathVariable("itemId") Long itemId) {

        service.deleteItem(hotelId, categoryId, itemId);
    }
}
