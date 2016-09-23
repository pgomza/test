package com.horeca.site.controllers;

import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.hotel.services.AvailableServices;
import com.horeca.site.models.hotel.services.AvailableServicesView;
import com.horeca.site.models.hotel.services.breakfast.Breakfast;
import com.horeca.site.models.hotel.services.breakfast.BreakfastView;
import com.horeca.site.services.AvailableServicesService;
import com.horeca.site.services.HotelService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Api(value = "hotels")
@RestController
@RequestMapping("/api/services")
public class AvailableServicesController {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private AvailableServicesService servicesService;

    @RequestMapping(value = "/{hotelId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public AvailableServicesView getAll(@PathVariable("hotelId") Long hotelId, HttpServletRequest request) {
        String language = request.getLocale().getLanguage();
        Hotel hotel = hotelService.get(hotelId);
        AvailableServices availableServices = servicesService.get(hotel);
        AvailableServicesView view = availableServices.toView(language, hotel.getDefaultTranslation());
        return view;
    }

    @RequestMapping(value = "/{hotelId}/breakfast", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public BreakfastView getBreakfast(@PathVariable("hotelId") Long hotelId, HttpServletRequest request) {
        String language = request.getLocale().getLanguage();
        Hotel hotel = hotelService.get(hotelId);
        Breakfast breakfast = hotel.getAvailableServices().getBreakfast();
        return breakfast.toView(language, hotel.getDefaultTranslation());
    }

    @RequestMapping(value = "/{hotelId}/breakfast", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public Breakfast updateBreakfast(@PathVariable("hotelId") Long hotelId, @Valid @RequestBody Breakfast newOne, HttpServletRequest request) {
        Hotel hotel = hotelService.get(hotelId);
        Breakfast oldOne = servicesService.getBreakfast(hotel);
        return servicesService.updateBreakfast(oldOne.getId(), newOne);
    }
}
