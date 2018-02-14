package com.horeca.site.controllers.services;

import com.horeca.site.handlers.HotelId;
import com.horeca.site.handlers.ReplaceCurrency;
import com.horeca.site.handlers.TranslateReturnValue;
import com.horeca.site.models.hotel.services.ServiceAvailability;
import com.horeca.site.models.hotel.services.breakfast.Breakfast;
import com.horeca.site.models.hotel.services.breakfast.BreakfastItemUpdate;
import com.horeca.site.services.services.BreakfastService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "hotels")
@RestController
@RequestMapping("/api/hotels")
public class BreakfastController {

    @Autowired
    private BreakfastService service;

    @ReplaceCurrency
    @TranslateReturnValue
    @RequestMapping(value = "/{hotelId}/services/breakfast", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Breakfast get(@HotelId @PathVariable("hotelId") Long hotelId) {
        return service.get(hotelId);
    }

    @RequestMapping(value = "/{hotelId}/services/breakfast/availability", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public Breakfast updateAvailability(@HotelId @PathVariable("hotelId") Long hotelId,
                                        @RequestBody ServiceAvailability availability) {
        return service.updateAvailability(hotelId, availability.getAvailable());
    }

    @RequestMapping(value = "/{hotelId}/services/breakfast/items", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void addItem(@PathVariable("hotelId") Long hotelId,
                                 @Valid @RequestBody BreakfastItemUpdate item) {
        service.addItem(hotelId, item);
    }

    //TODO path variables should not be empty
    @RequestMapping(value = "/{hotelId}/services/breakfast/items/{itemId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateItem(@PathVariable("hotelId") Long hotelId,
                           @PathVariable("itemId") Long itemId,
                           @Valid @RequestBody BreakfastItemUpdate item) {
        item.setId(itemId);
        service.updateItem(hotelId, item);
    }

    //TODO path variables should not be empty
    @RequestMapping(value = "/{hotelId}/services/breakfast/items/{itemId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteItem(@PathVariable("hotelId") Long hotelId,
                           @PathVariable("itemId") Long itemId) {
        service.deleteItem(itemId);
    }
}
