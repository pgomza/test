package com.horeca.site.controllers.services;

import com.horeca.site.models.hotel.services.spa.SpaView;
import com.horeca.site.models.hotel.services.spa.calendar.SpaCalendarHour;
import com.horeca.site.services.SpaService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(value = "hotels")
@RestController
@RequestMapping("/api/hotels")
public class SpaController {

    @Autowired
    private SpaService spaService;

    @RequestMapping(value = "/{hotelId}/services/spa", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public SpaView get(@PathVariable("hotelId") Long hotelId, HttpServletRequest request) {
        String language = request.getLocale().getLanguage();
        return spaService.getView(hotelId, language);
    }

    @RequestMapping(value = "/{hotelId}/services/spa/items/{itemId}/calendar", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SpaCalendarHour> getHours(@PathVariable("hotelId") Long hotelId,
                                          @PathVariable("itemId") Long itemId,
                                          @RequestParam("date") String date) { //TODO change the type to LocalDate
        return spaService.getCalendarHours(hotelId, itemId, date);
    }
}
