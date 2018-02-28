package com.horeca.site.controllers.services;

import com.horeca.site.handlers.HotelId;
import com.horeca.site.handlers.TranslateReturnValue;
import com.horeca.site.models.hotel.services.ServiceAvailabilityImpl;
import com.horeca.site.models.hotel.services.hairdresser.HairDresser;
import com.horeca.site.services.services.HairDresserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/{hotelId}/services/hairdresser/availability", method = RequestMethod.PUT, produces =
            MediaType.APPLICATION_JSON_VALUE)
    public HairDresser updateAvailability(@HotelId @PathVariable("hotelId") Long hotelId,
                                          @RequestBody ServiceAvailabilityImpl availability) {
        return service.updateAvailability(hotelId, availability.getAvailable());
    }

}
