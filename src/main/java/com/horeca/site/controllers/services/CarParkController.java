package com.horeca.site.controllers.services;

import com.horeca.site.handlers.HotelId;
import com.horeca.site.handlers.TranslateReturnValue;
import com.horeca.site.models.hotel.services.carpark.CarPark;
import com.horeca.site.services.services.CarParkService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "hotels")
@RestController
@RequestMapping("/api/hotels")
public class CarParkController {

    @Autowired
    private CarParkService carParkService;

    @TranslateReturnValue
    @RequestMapping(value = "/{hotelId}/services/carpark", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public CarPark get(@HotelId @PathVariable("hotelId") Long hotelId) {
        return carParkService.get(hotelId);
    }

    @RequestMapping(value = "/{hotelId}/services/carpark", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public CarPark addDefault(@PathVariable("hotelId") Long hotelId) {
        return carParkService.addDefaultCarPark(hotelId);
    }
}
