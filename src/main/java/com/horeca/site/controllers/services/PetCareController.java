package com.horeca.site.controllers.services;

import com.horeca.site.handlers.HotelId;
import com.horeca.site.handlers.ReplaceCurrency;
import com.horeca.site.handlers.TranslateReturnValue;
import com.horeca.site.models.hotel.services.ServiceAvailability;
import com.horeca.site.models.hotel.services.petcare.PetCare;
import com.horeca.site.models.hotel.services.petcare.PetCareItem;
import com.horeca.site.services.services.PetCareService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(value = "hotels")
@RestController
@RequestMapping("/api/hotels")
public class PetCareController {

    @Autowired
    private PetCareService service;

    @ReplaceCurrency
    @TranslateReturnValue
    @RequestMapping(value = "/{hotelId}/services/petcare", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public PetCare get(@HotelId @PathVariable("hotelId") Long hotelId) {
        return service.get(hotelId);
    }

    @ReplaceCurrency
    @TranslateReturnValue
    @RequestMapping(value = "/{hotelId}/services/petcare/items", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PetCareItem> getItems(@HotelId @PathVariable("hotelId") Long hotelId) {
        return service.getItems(hotelId);
    }

    @ReplaceCurrency
    @TranslateReturnValue
    @RequestMapping(value = "/{hotelId}/services/petcare/items/{itemId}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public PetCareItem getItem(@HotelId @PathVariable("hotelId") Long hotelId, @PathVariable("itemId") Long itemId) {
        return service.getItem(hotelId, itemId);
    }

    @RequestMapping(value = "/{hotelId}/services/petcare/availability", method = RequestMethod.PUT, produces = MediaType
            .APPLICATION_JSON_VALUE)
    public PetCare updateAvailability(@HotelId @PathVariable("hotelId") Long hotelId,
                                      @RequestBody ServiceAvailability availability) {
        return service.updateAvailability(hotelId, availability.getAvailable());
    }

    @RequestMapping(value = "/{hotelId}/services/petcare/items", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public PetCareItem addItem(@PathVariable("hotelId") Long hotelId, @Valid @RequestBody PetCareItem item) {
        return service.addItem(hotelId, item);
    }

    @RequestMapping(value = "/{hotelId}/services/petcare/items/{itemId}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public PetCareItem updateItem(@PathVariable("hotelId") Long hotelId,
                                  @PathVariable("itemId") Long itemId,
                                  @Valid @RequestBody PetCareItem item) {
        item.setId(itemId);
        return service.updateItem(hotelId, item);
    }

    @RequestMapping(value = "/{hotelId}/services/petcare/items/{itemId}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteItem(@PathVariable("hotelId") Long hotelId,
                                  @PathVariable("itemId") Long itemId) {
        service.deleteItem(hotelId, itemId);
    }
}
