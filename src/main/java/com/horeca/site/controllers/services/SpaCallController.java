package com.horeca.site.controllers.services;

import com.horeca.site.handlers.HotelId;
import com.horeca.site.handlers.TranslateReturnValue;
import com.horeca.site.models.hotel.services.ServiceAvailabilityImpl;
import com.horeca.site.models.hotel.services.spacall.SpaCall;
import com.horeca.site.services.services.SpaCallService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Api(value = "hotels")
@RestController
@RequestMapping("/api/hotels")
public class SpaCallController {

    @Autowired
    private SpaCallService service;

    @TranslateReturnValue
    @RequestMapping(value = "/{hotelId}/services/spacall", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public SpaCall get(@HotelId @PathVariable("hotelId") Long hotelId) {
        return service.get(hotelId);
    }

    @RequestMapping(value = "/{hotelId}/services/spacall/availability", method = RequestMethod.PUT, produces =
            MediaType.APPLICATION_JSON_VALUE)
    public SpaCall updateAvailability(@HotelId @PathVariable("hotelId") Long hotelId,
                                             @RequestBody ServiceAvailabilityImpl availability) {
        return service.updateAvailability(hotelId, availability.getAvailable());
    }
}
