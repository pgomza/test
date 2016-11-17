package com.horeca.site.controllers.services;

import com.horeca.annotations.AllowCORS;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.hotel.services.breakfast.BreakfastCategory;
import com.horeca.site.models.hotel.services.breakfast.BreakfastItem;
import com.horeca.site.models.hotel.services.breakfast.BreakfastItemUpdate;
import com.horeca.site.models.hotel.services.breakfast.BreakfastView;
import com.horeca.site.services.services.BreakfastService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Api(value = "hotels")
@AllowCORS
@RestController
@RequestMapping("/api/hotels")
public class BreakfastController {

    @Autowired
    private BreakfastService breakfastService;

    @RequestMapping(value = "/{hotelId}/services/breakfast", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public BreakfastView get(@PathVariable("hotelId") Long hotelId, HttpServletRequest request) {
        String language = request.getLocale().getLanguage();
        return breakfastService.getView(hotelId, language);
    }

    @RequestMapping(value = "/{hotelId}/services/breakfast/items", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void addItem(@PathVariable("hotelId") Long hotelId,
                                 @Valid @RequestBody BreakfastItemUpdate item) {
        breakfastService.addItem(hotelId, item);
    }

    //TODO path variables should not be empty
    @RequestMapping(value = "/{hotelId}/services/breakfast/items/{itemId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateItem(@PathVariable("hotelId") Long hotelId,
                           @PathVariable("itemId") Long itemId,
                           @Valid @RequestBody BreakfastItemUpdate item) {
        item.setId(itemId);
        breakfastService.updateItem(hotelId, item);
    }

    //TODO path variables should not be empty
    @RequestMapping(value = "/{hotelId}/services/breakfast/items/{itemId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteItem(@PathVariable("hotelId") Long hotelId,
                           @PathVariable("itemId") Long itemId) {
        breakfastService.deleteItem(itemId);
    }
}
