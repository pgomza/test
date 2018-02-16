package com.horeca.site.controllers.services;

import com.horeca.site.handlers.HotelId;
import com.horeca.site.handlers.ReplaceCurrency;
import com.horeca.site.handlers.TranslateReturnValue;
import com.horeca.site.models.hotel.services.ServiceAvailability;
import com.horeca.site.models.hotel.services.carpark.CarPark;
import com.horeca.site.services.services.CarParkService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Api(value = "hotels")
@RestController
@RequestMapping("/api/hotels")
public class CarParkController {

    @Autowired
    private CarParkService service;

    @ReplaceCurrency
    @TranslateReturnValue
    @RequestMapping(value = "/{hotelId}/services/carpark", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public CarPark get(@HotelId @PathVariable("hotelId") Long hotelId) {
        return service.get(hotelId);
    }

    @RequestMapping(value = "/{hotelId}/services/carpark/availability", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public CarPark updateAvailability(@HotelId @PathVariable("hotelId") Long hotelId,
                                      @RequestBody ServiceAvailability availability) {
        return service.updateAvailability(hotelId, availability.getAvailable());
    }

}
