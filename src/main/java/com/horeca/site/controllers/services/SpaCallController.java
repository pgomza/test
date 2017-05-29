package com.horeca.site.controllers.services;

import com.horeca.site.models.hotel.services.spacall.SpaCall;
import com.horeca.site.services.services.SpaCallService;
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
public class SpaCallController {

    @Autowired
    private SpaCallService service;

    @RequestMapping(value = "/{hotelId}/services/spacall", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public SpaCall get(@PathVariable("hotelId") Long hotelId) {
        return service.get(hotelId);
    }

    @RequestMapping(value = "/{hotelId}/services/spacall", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public SpaCall addDefault(@PathVariable("hotelId") Long hotelId) {
        return service.addDefaultSpaCall(hotelId);
    }
}
