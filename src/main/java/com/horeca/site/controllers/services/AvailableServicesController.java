package com.horeca.site.controllers.services;

import com.horeca.annotations.AllowCORS;
import com.horeca.site.models.hotel.services.AvailableServices;
import com.horeca.site.services.services.AvailableServicesService;
import com.horeca.site.services.HotelService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "hotels")
@AllowCORS
@RestController
@RequestMapping("/api/hotels")
public class AvailableServicesController {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private AvailableServicesService service;

    @RequestMapping(value = "/{hotelId}/services", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public AvailableServices getAll(@PathVariable("hotelId") Long hotelId) {
        return service.get(hotelId);
    }
}
