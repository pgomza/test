package com.horeca.site.controllers.services;

import com.horeca.annotations.AllowCORS;
import com.horeca.site.models.hotel.services.AvailableServicesView;
import com.horeca.site.services.services.AvailableServicesService;
import com.horeca.site.services.HotelService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
    public AvailableServicesView getAll(@PathVariable("hotelId") Long hotelId, HttpServletRequest request) {
        String preferredLanguage = request.getLocale().getLanguage();
        return service.getView(hotelId, preferredLanguage);
    }
}
