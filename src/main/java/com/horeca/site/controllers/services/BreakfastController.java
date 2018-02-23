package com.horeca.site.controllers.services;

import com.horeca.site.handlers.HotelId;
import com.horeca.site.handlers.ReplaceCurrency;
import com.horeca.site.handlers.TranslateReturnValue;
import com.horeca.site.models.hotel.services.StandardServiceCategoryModelPatch;
import com.horeca.site.models.hotel.services.StandardServiceModelPatch;
import com.horeca.site.models.hotel.services.breakfast.Breakfast;
import com.horeca.site.models.hotel.services.breakfast.BreakfastCategory;
import com.horeca.site.models.hotel.services.breakfast.BreakfastItem;
import com.horeca.site.services.services.BreakfastService;
import io.swagger.annotations.Api;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "hotels")
@RestController
@RequestMapping("/api/hotels")
public class BreakfastController {

    @Autowired
    private BreakfastService service;

    @ReplaceCurrency
    @TranslateReturnValue
    @RequestMapping(value = "/{hotelId}/services/breakfast", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Breakfast get(@HotelId @PathVariable("hotelId") Long hotelId) {
        return service.get(hotelId);
    }

    @RequestMapping(value = "/{hotelId}/services/breakfast", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Breakfast update(@PathVariable("hotelId") Long hotelId, @RequestBody Breakfast menu) {
        return service.update(hotelId, menu);
    }

    @RequestMapping(value = "/{hotelId}/services/breakfast", method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Breakfast patch(@PathVariable("hotelId") Long hotelId, @RequestBody StandardServiceModelPatch patch) {
        return service.patch(hotelId, patch);
    }

    @RequestMapping(value = "/{hotelId}/services/breakfast", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Breakfast addDefault(@PathVariable("hotelId") Long hotelId) {
        return service.addDefaultBreakfast(hotelId);
    }

    @ReplaceCurrency
    @TranslateReturnValue
    @RequestMapping(value = "/{hotelId}/services/breakfast/categories", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BreakfastCategory> getCategories(@HotelId @PathVariable("hotelId") Long hotelId) {
        return service.getCategories(hotelId);
    }

    @RequestMapping(value = "/{hotelId}/services/breakfast/categories", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public BreakfastCategory addCategory(@PathVariable("hotelId") Long hotelId, @RequestBody BreakfastCategory category) {
        return service.addCategory(hotelId, category);
    }

    @ReplaceCurrency
    @TranslateReturnValue
    @RequestMapping(value = "/{hotelId}/services/breakfast/categories/{categoryId}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public BreakfastCategory getCategory(@HotelId @PathVariable("hotelId") Long hotelId,
                                   @PathVariable("categoryId") Long categoryId) {
        return service.getCategory(hotelId, categoryId);
    }

    @RequestMapping(value = "/{hotelId}/services/breakfast/categories/{categoryId}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public BreakfastCategory updateCategory(@PathVariable("hotelId") Long hotelId,
                                      @PathVariable("categoryId") Long categoryId,
                                      @RequestBody BreakfastCategory category) {
        return service.updateCategory(hotelId, categoryId, category);
    }

    @RequestMapping(value = "/{hotelId}/services/breakfast/categories/{categoryId}", method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public BreakfastCategory patchCategory(@PathVariable("hotelId") Long hotelId,
                                     @PathVariable("categoryId") Long categoryId,
                                     @RequestBody CategoryPatch patch) {
        return service.patchCategory(hotelId, categoryId, new StandardServiceCategoryModelPatch(patch.getCategory()));
    }

    @RequestMapping(value = "/{hotelId}/services/breakfast/categories/{categoryId}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteCategory(@PathVariable("hotelId") Long hotelId,
                               @PathVariable("categoryId") Long categoryId) {
        service.deleteCategory(hotelId, categoryId);
    }

    @ReplaceCurrency
    @TranslateReturnValue
    @RequestMapping(value = "/{hotelId}/services/breakfast/categories/{categoryId}/items",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BreakfastItem> getItems(@HotelId @PathVariable("hotelId") Long hotelId,
                                        @PathVariable("categoryId") Long categoryId) {

        return service.getItems(hotelId, categoryId);
    }

    @RequestMapping(value = "/{hotelId}/services/breakfast/categories/{categoryId}/items",
            method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public BreakfastItem addItem(@PathVariable("hotelId") Long hotelId,
                           @PathVariable("categoryId") Long categoryId,
                           @RequestBody BreakfastItem item) {

        return service.addItem(hotelId, categoryId, item);
    }

    @ReplaceCurrency
    @TranslateReturnValue
    @RequestMapping(value = "/{hotelId}/services/breakfast/categories/{categoryId}/items/{itemId}",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public BreakfastItem getItem(@HotelId @PathVariable("hotelId") Long hotelId,
                           @PathVariable("categoryId") Long categoryId,
                           @PathVariable("itemId") Long itemId) {

        return service.getItem(hotelId, categoryId, itemId);
    }

    @RequestMapping(value = "/{hotelId}/services/breakfast/categories/{categoryId}/items/{itemId}",
            method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public BreakfastItem updateItem(@PathVariable("hotelId") Long hotelId,
                              @PathVariable("categoryId") Long categoryId,
                              @PathVariable("itemId") Long itemId,
                              @RequestBody BreakfastItem item) {

        item.setId(itemId);
        return service.updateItem(hotelId, categoryId, item);
    }

    @RequestMapping(value = "/{hotelId}/services/breakfast/categories/{categoryId}/items/{itemId}",
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
