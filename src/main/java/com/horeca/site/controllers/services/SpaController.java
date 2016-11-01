package com.horeca.site.controllers.services;

import com.horeca.annotations.AllowCORS;
import com.horeca.site.models.hotel.services.spa.SpaView;
import com.horeca.site.models.hotel.services.spa.calendar.SpaCalendarHour;
import com.horeca.site.services.services.SpaService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Set;

@Api(value = "hotels")
@AllowCORS
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

    @RequestMapping(value = "/{hotelId}/services/spa/items/{itemId}/calendar",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<SpaCalendarHour> getHoursOld(@PathVariable("hotelId") Long hotelId,
                                         @PathVariable("itemId") Long itemId,
                                         @RequestParam("date") String date) { //TODO change the type to LocalDate
        return this.getHours(hotelId, itemId, date);
    }

    @RequestMapping(value = "/{hotelId}/services/spa/items/{itemId}/calendar/{date}",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<SpaCalendarHour> getHours(@PathVariable("hotelId") Long hotelId,
                                         @PathVariable("itemId") Long itemId,
                                         @PathVariable("date") String date) { //TODO change the type to LocalDate
        return spaService.getCalendarHours(hotelId, itemId, date);
    }

    @RequestMapping(value = "/{hotelId}/services/spa/items/{itemId}/calendar/{date}",
            method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<SpaCalendarHour> updateHours(@PathVariable("hotelId") Long hotelId,
                                            @PathVariable("itemId") Long itemId,
                                            @PathVariable("date") String date,
                                            @Valid @RequestBody Set<SpaCalendarHour> hours) { //TODO change the type to LocalDate
        return spaService.updateCalendarHours(hotelId, itemId, date, hours);
    }

}
