package com.horeca.site.controllers;

import com.horeca.site.models.guest.Guest;
import com.horeca.site.services.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/hotels")
public class GuestController {

    @Autowired
    private GuestService service;

    @RequestMapping(value = "/{hotelId}/guests", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<Guest> getAll(@PathVariable("hotelId") Long hotelId) {
        return service.getAll(hotelId);
    }

    @RequestMapping(value = "/{hotelId}/guests/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Guest get(@PathVariable("hotelId") Long hotelId, @PathVariable("id") Long id) {
        return service.get(hotelId, id);
    }

    @RequestMapping(value = "/{hotelId}/guests", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Guest add(@PathVariable("hotelId") Long hotelId, @RequestBody Guest guest) {
        return service.save(hotelId, guest);
    }

    @RequestMapping(value = "/{hotelId}/guests/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public Guest update(@PathVariable("hotelId") Long hotelId, @PathVariable("id") Long id, @RequestBody Guest guest) {
        guest.setId(id);
        return service.save(hotelId, guest);
    }

    @RequestMapping(value = "/{hotelId}/guests/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable("hotelId") Long hotelId, @PathVariable("id") Long id) {
        service.delete(hotelId, id);
    }
}
