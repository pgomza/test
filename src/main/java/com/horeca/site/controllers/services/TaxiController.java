package com.horeca.site.controllers.services;

import com.horeca.site.models.hotel.services.taxi.Taxi;
import com.horeca.site.services.TaxiService;
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
public class TaxiController {

    @Autowired
    private TaxiService taxiService;

    @RequestMapping(value = "/{hotelId}/services/taxi", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Taxi get(@PathVariable("hotelId") Long hotelId) {
        return taxiService.get(hotelId);
    }
}
