package com.horeca.site.controllers.services;

import com.horeca.site.models.hotel.services.taxi.Taxi;
import com.horeca.site.models.hotel.services.taxi.TaxiItem;
import com.horeca.site.services.TaxiService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "hotels")
@CrossOrigin(methods = { RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST,
        RequestMethod.DELETE, RequestMethod.HEAD, RequestMethod.PATCH, RequestMethod.OPTIONS })
@RestController
@RequestMapping("/api/hotels")
public class TaxiController {

    @Autowired
    private TaxiService taxiService;

    @RequestMapping(value = "/{hotelId}/services/taxi", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Taxi get(@PathVariable("hotelId") Long hotelId) {
        return taxiService.get(hotelId);
    }

    @RequestMapping(value = "/{hotelId}/services/taxi/items", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<TaxiItem> addItem(@PathVariable("hotelId") Long hotelId) {
        return taxiService.getItems(hotelId);
    }

    // TODO should return TaxiItem instead of Taxi
    @RequestMapping(value = "/{hotelId}/services/taxi/items", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Taxi addItem(@PathVariable("hotelId") Long hotelId, @Valid @RequestBody TaxiItem item) {
        return taxiService.addItem(hotelId, item);
    }

    // TODO should return TaxiItem instead of Taxi
    @RequestMapping(value = "/{hotelId}/services/taxi/items/{itemId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public Taxi updateItem(@PathVariable("hotelId") Long hotelId,
                        @PathVariable("itemId") Long itemId,
                        @Valid @RequestBody TaxiItem item) {
        item.setId(itemId);
        return taxiService.updateItem(hotelId, item);
    }

    @RequestMapping(value = "/{hotelId}/services/taxi/items/{itemId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteItem(@PathVariable("hotelId") Long hotelId,
                           @PathVariable("itemId") Long itemId) {
        taxiService.deleteItem(hotelId, itemId);
    }
}
