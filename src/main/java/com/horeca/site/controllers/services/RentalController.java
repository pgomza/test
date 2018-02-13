package com.horeca.site.controllers.services;

import com.horeca.site.handlers.HotelId;
import com.horeca.site.handlers.ReplaceCurrency;
import com.horeca.site.handlers.TranslateReturnValue;
import com.horeca.site.models.hotel.services.rental.Rental;
import com.horeca.site.models.hotel.services.rental.RentalItem;
import com.horeca.site.models.hotel.services.rental.RentalItemUpdate;
import com.horeca.site.services.services.RentalService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "hotels")
@RestController
@RequestMapping("/api/hotels")
public class RentalController {

    @Autowired
    private RentalService service;

    @ReplaceCurrency
    @TranslateReturnValue
    @RequestMapping(value = "/{hotelId}/services/rental", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Rental get(@HotelId @PathVariable("hotelId") Long hotelId) {
        return service.get(hotelId);
    }

    @RequestMapping(value = "/{hotelId}/services/rental/items", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void addItem(@PathVariable("hotelId") Long hotelId,
                        @Valid @RequestBody RentalItemUpdate item) {
        service.addItem(hotelId, item);
    }

    //TODO path variables should not be empty
    @RequestMapping(value = "/{hotelId}/services/rental/items/{itemId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public RentalItem updateItem(@PathVariable("hotelId") Long hotelId,
                                 @PathVariable("itemId") Long itemId,
                                 @Valid @RequestBody RentalItemUpdate item) {
        item.setId(itemId);
        return service.updateItem(hotelId, item);
    }

    //TODO path variables should not be empty
    @RequestMapping(value = "/{hotelId}/services/rental/items/{itemId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteItem(@PathVariable("hotelId") Long hotelId,
                           @PathVariable("itemId") Long itemId) {
        service.deleteItem(hotelId, itemId);
    }
}
