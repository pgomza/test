package com.horeca.site.controllers.services;

import com.horeca.site.handlers.HotelId;
import com.horeca.site.handlers.TranslateReturnValue;
import com.horeca.site.models.hotel.services.hairdresser.HairDresser;
import com.horeca.site.services.services.HairDresserService;
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
public class HairDresserController {

    @Autowired
    private HairDresserService service;

    @TranslateReturnValue
    @RequestMapping(value = "/{hotelId}/services/hairdresser", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public HairDresser get(@HotelId @PathVariable("hotelId") Long hotelId) {
        return service.get(hotelId);
    }

    @RequestMapping(value = "/{hotelId}/services/hairdresser", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public HairDresser addDefault(@PathVariable("hotelId") Long hotelId) {
        return service.addDefaultHairDresser(hotelId);
    }
}
