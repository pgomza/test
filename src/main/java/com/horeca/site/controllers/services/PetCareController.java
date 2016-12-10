package com.horeca.site.controllers.services;

import com.horeca.annotations.AllowCORS;
import com.horeca.site.models.hotel.services.petcare.PetCare;
import com.horeca.site.models.hotel.services.petcare.PetCareItem;
import com.horeca.site.models.hotel.services.petcare.calendar.PetCareCalendarHour;
import com.horeca.site.services.services.PetCareService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@Api(value = "hotels")
@AllowCORS
@RestController
@RequestMapping("/api/hotels")
public class PetCareController {

    @Autowired
    private PetCareService petCareService;

    @RequestMapping(value = "/{hotelId}/services/petcare", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public PetCare get(@PathVariable("hotelId") Long hotelId) {
        return petCareService.get(hotelId);
    }

    @RequestMapping(value = "/{hotelId}/services/petcare/items", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public PetCareItem addItem(@PathVariable("hotelId") Long hotelId, @Valid @RequestBody PetCareItem item) {
        return petCareService.addItem(hotelId, item);
    }

    @RequestMapping(value = "/{hotelId}/services/petcare/items/{itemId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public PetCareItem updateItem(@PathVariable("hotelId") Long hotelId,
                                  @PathVariable("itemId") Long itemId,
                                  @Valid @RequestBody PetCareItem item) {
        item.setId(itemId);
        return petCareService.updateItem(item);
    }

    @RequestMapping(value = "/{hotelId}/services/petcare/items/{itemId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteItem(@PathVariable("hotelId") Long hotelId,
                                  @PathVariable("itemId") Long itemId) {
        petCareService.deleteItem(itemId);
    }

    @RequestMapping(value = "/{hotelId}/services/petcare/items/{itemId}/calendar",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<PetCareCalendarHour> getHoursOld(@PathVariable("hotelId") Long hotelId,
                                                @PathVariable("itemId") Long itemId,
                                                @RequestParam("date") String date) { //TODO change the type to LocalDate
        return this.getHours(hotelId, itemId, date);
    }

    @RequestMapping(value = "/{hotelId}/services/petcare/items/{itemId}/calendar/{date}",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<PetCareCalendarHour> getHours(@PathVariable("hotelId") Long hotelId,
                                             @PathVariable("itemId") Long itemId,
                                             @PathVariable("date") String date) { //TODO change the type to LocalDate
        return petCareService.getCalendarHours(hotelId, itemId, date);
    }

    @RequestMapping(value = "/{hotelId}/services/petcare/items/{itemId}/calendar/{date}",
            method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<PetCareCalendarHour> updateHours(@PathVariable("hotelId") Long hotelId,
                                                @PathVariable("itemId") Long itemId,
                                                @PathVariable("date") String date,
                                                @Valid @RequestBody Set<PetCareCalendarHour> hours) { //TODO change the type to LocalDate
        return petCareService.updateCalendarHours(hotelId, itemId, date, hours);
    }
}
