package com.horeca.site.controllers.services;

import com.horeca.site.handlers.HotelId;
import com.horeca.site.handlers.TranslateReturnValue;
import com.horeca.site.models.hotel.services.AvailableServices;
import com.horeca.site.models.hotel.translation.LanguageCode;
import com.horeca.site.services.services.AvailableServicesService;
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
public class AvailableServicesController {

    @Autowired
    private AvailableServicesService service;

    @TranslateReturnValue
    @RequestMapping(value = "/{hotelId}/services", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public AvailableServices getAll(@HotelId @PathVariable("hotelId") Long hotelId, LanguageCode languageCode) {
        return service.get(hotelId);
    }
}
