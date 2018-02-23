package com.horeca.site.controllers.services;

import com.horeca.site.handlers.HotelId;
import com.horeca.site.handlers.ReplaceCurrency;
import com.horeca.site.handlers.TranslateReturnValue;
import com.horeca.site.models.hotel.services.ServiceAvailability;
import com.horeca.site.models.hotel.services.StandardServiceCategoryModelPatch;
import com.horeca.site.models.hotel.services.StandardServiceModelPatch;
import com.horeca.site.models.hotel.services.bar.Bar;
import com.horeca.site.models.hotel.services.bar.BarCategory;
import com.horeca.site.models.hotel.services.bar.BarItem;
import com.horeca.site.services.services.BarService;
import io.swagger.annotations.Api;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "hotels")
@RestController
@RequestMapping("/api/hotels")
public class BarController {

    @Autowired
    private BarService service;

    @ReplaceCurrency
    @TranslateReturnValue
    @RequestMapping(value = "/{hotelId}/services/bar", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Bar get(@HotelId @PathVariable("hotelId") Long hotelId) {
        return service.get(hotelId);
    }

    @RequestMapping(value = "/{hotelId}/services/bar/availability", method = RequestMethod.PUT, produces = MediaType
            .APPLICATION_JSON_VALUE)
    public Bar updateAvailability(@HotelId @PathVariable("hotelId") Long hotelId, @RequestBody ServiceAvailability availability) {
        return service.updateAvailability(hotelId, availability.getAvailable());
    }

    @RequestMapping(value = "/{hotelId}/services/bar", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Bar update(@PathVariable("hotelId") Long hotelId, @RequestBody Bar menu) {
        return service.update(hotelId, menu);
    }

    @RequestMapping(value = "/{hotelId}/services/bar", method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Bar patch(@PathVariable("hotelId") Long hotelId, @RequestBody StandardServiceModelPatch patch) {
        return service.patch(hotelId, patch);
    }

    @ReplaceCurrency
    @TranslateReturnValue
    @RequestMapping(value = "/{hotelId}/services/bar/categories", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BarCategory> getCategories(@HotelId @PathVariable("hotelId") Long hotelId) {
        return service.getCategories(hotelId);
    }

    @RequestMapping(value = "/{hotelId}/services/bar/categories", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public BarCategory addCategory(@PathVariable("hotelId") Long hotelId, @RequestBody BarCategory category) {
        return service.addCategory(hotelId, category);
    }

    @ReplaceCurrency
    @TranslateReturnValue
    @RequestMapping(value = "/{hotelId}/services/bar/categories/{categoryId}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public BarCategory getCategory(@HotelId @PathVariable("hotelId") Long hotelId,
                                                    @PathVariable("categoryId") Long categoryId) {
        return service.getCategory(hotelId, categoryId);
    }

    @RequestMapping(value = "/{hotelId}/services/bar/categories/{categoryId}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public BarCategory updateCategory(@PathVariable("hotelId") Long hotelId,
                                                 @PathVariable("categoryId") Long categoryId,
                                                 @RequestBody BarCategory category) {
        return service.updateCategory(hotelId, categoryId, category);
    }

    @RequestMapping(value = "/{hotelId}/services/bar/categories/{categoryId}", method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public BarCategory patchCategory(@PathVariable("hotelId") Long hotelId,
                                                @PathVariable("categoryId") Long categoryId,
                                                @RequestBody CategoryPatch patch) {
        return service.patchCategory(hotelId, categoryId, new StandardServiceCategoryModelPatch(patch.getCategory()));
    }

    @RequestMapping(value = "/{hotelId}/services/bar/categories/{categoryId}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteCategory(@PathVariable("hotelId") Long hotelId,
                               @PathVariable("categoryId") Long categoryId) {
        service.deleteCategory(hotelId, categoryId);
    }

    @ReplaceCurrency
    @TranslateReturnValue
    @RequestMapping(value = "/{hotelId}/services/bar/categories/{categoryId}/items",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BarItem> getItems(@HotelId @PathVariable("hotelId") Long hotelId,
                                             @PathVariable("categoryId") Long categoryId) {

        return service.getItems(hotelId, categoryId);
    }

    @RequestMapping(value = "/{hotelId}/services/bar/categories/{categoryId}/items",
            method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public BarItem addItem(@PathVariable("hotelId") Long hotelId,
                                      @PathVariable("categoryId") Long categoryId,
                                      @RequestBody BarItem item) {

        return service.addItem(hotelId, categoryId, item);
    }

    @ReplaceCurrency
    @TranslateReturnValue
    @RequestMapping(value = "/{hotelId}/services/bar/categories/{categoryId}/items/{itemId}",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public BarItem getItem(@HotelId @PathVariable("hotelId") Long hotelId,
                                      @PathVariable("categoryId") Long categoryId,
                                      @PathVariable("itemId") Long itemId) {

        return service.getItem(hotelId, categoryId, itemId);
    }

    @RequestMapping(value = "/{hotelId}/services/bar/categories/{categoryId}/items/{itemId}",
            method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public BarItem updateItem(@PathVariable("hotelId") Long hotelId,
                                         @PathVariable("categoryId") Long categoryId,
                                         @PathVariable("itemId") Long itemId,
                                         @RequestBody BarItem item) {

        item.setId(itemId);
        return service.updateItem(hotelId, categoryId, item);
    }

    @RequestMapping(value = "/{hotelId}/services/bar/categories/{categoryId}/items/{itemId}",
            method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteItem(@PathVariable("hotelId") Long hotelId,
                           @PathVariable("categoryId") Long categoryId,
                           @PathVariable("itemId") Long itemId) {

        service.deleteItem(hotelId, categoryId, itemId);
    }

    // to preserve backward compatibility
    public static class CategoryPatch {

        @NotEmpty
        private String category;

        CategoryPatch() {}

        public CategoryPatch(String category) {
            this.category = category;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }
    }
}
