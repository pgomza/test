package com.horeca.site.controllers.services;

import com.horeca.annotations.AllowCORS;
import com.horeca.site.models.hotel.services.breakfast.BreakfastView;
import com.horeca.site.services.services.BreakfastService;
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
public class BreakfastController {

    @Autowired
    private BreakfastService breakfastService;

    @RequestMapping(value = "/{hotelId}/services/breakfast", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public BreakfastView get(@PathVariable("hotelId") Long hotelId, HttpServletRequest request) {
        String language = request.getLocale().getLanguage();
        return breakfastService.getView(hotelId, language);
    }
}
