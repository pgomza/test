package com.horeca.site.controllers.services;

import com.horeca.site.models.hotel.services.housekeeping.Housekeeping;
import com.horeca.site.models.hotel.services.housekeeping.HousekeepingItem;
import com.horeca.site.services.services.HousekeepingService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "hotels")
@RestController
@RequestMapping("/api/hotels")
public class HousekeepingController {

    @Autowired
    private HousekeepingService service;

    @RequestMapping(value = "/{hotelId}/services/housekeeping", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Housekeeping get(@PathVariable("hotelId") Long hotelId) {
        return service.get(hotelId);
    }

    @RequestMapping(value = "/{hotelId}/services/housekeeping/items", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<HousekeepingItem> getItems(@PathVariable("hotelId") Long hotelId) {
        return service.getItems(hotelId);
    }

    // TODO should return HousekeepingItem instead of Housekeeping
    @RequestMapping(value = "/{hotelId}/services/housekeeping/items", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Housekeeping addItem(@PathVariable("hotelId") Long hotelId, @Valid @RequestBody HousekeepingItem item) {
        return service.addItem(hotelId, item);
    }

    @RequestMapping(value = "/{hotelId}/services/housekeeping/items/{itemId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public Housekeeping updateItem(@PathVariable("hotelId") Long hotelId,
                           @PathVariable("itemId") Long itemId,
                           @Valid @RequestBody HousekeepingItem item) {
        item.setId(itemId);
        return service.updateItem(hotelId, item);
    }

    @RequestMapping(value = "/{hotelId}/services/housekeeping/items/{itemId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteItem(@PathVariable("hotelId") Long hotelId,
                           @PathVariable("itemId") Long itemId) {
        service.deleteItem(hotelId, itemId);
    }
}
