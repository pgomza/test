package com.horeca.site.controllers.services;

import com.horeca.site.models.hotel.services.restaurantmenu.RestaurantMenu;
import com.horeca.site.models.hotel.services.restaurantmenu.RestaurantMenuCategory;
import com.horeca.site.models.hotel.services.restaurantmenu.RestaurantMenuItem;
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

    @RequestMapping(value = "/{hotelId}/services/restaurantmenu/categories", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RestaurantMenuCategory> getCategories(@PathVariable("hotelId") Long hotelId) {
        return service.getCategories(hotelId);
    }

    @RequestMapping(value = "/{hotelId}/services/restaurantmenu/categories", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RestaurantMenuCategory addCategory(@PathVariable("hotelId") Long hotelId, @RequestBody RestaurantMenuCategory category) {
        return service.addCategory(hotelId, category);
    }

    @RequestMapping(value = "/{hotelId}/services/restaurantmenu/categories/{categoryId}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RestaurantMenuCategory getCategory(@PathVariable("hotelId") Long hotelId,
                                                @PathVariable("categoryId") Long categoryId) {
        return service.getCategory(hotelId, categoryId);
    }

    @RequestMapping(value = "/{hotelId}/services/restaurantmenu/categories/{categoryId}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RestaurantMenuCategory updateCategory(@PathVariable("hotelId") Long hotelId,
                                                 @PathVariable("categoryId") Long categoryId,
                                                 @RequestBody RestaurantMenuCategory category) {
        category.setId(categoryId);
        return service.updateCategory(hotelId, category);
    }

    @RequestMapping(value = "/{hotelId}/services/restaurantmenu/categories/{categoryId}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteCategory(@PathVariable("hotelId") Long hotelId,
                                                 @PathVariable("categoryId") Long categoryId) {
        service.deleteCategory(hotelId, categoryId);
    }

    @RequestMapping(value = "/{hotelId}/services/restaurantmenu/categories/{categoryId}/items",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RestaurantMenuItem> getItems(@PathVariable("hotelId") Long hotelId,
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

    @RequestMapping(value = "/{hotelId}/services/restaurantmenu/categories/{categoryId}/items/{itemId}",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public RestaurantMenuItem getItem(@PathVariable("hotelId") Long hotelId,
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
