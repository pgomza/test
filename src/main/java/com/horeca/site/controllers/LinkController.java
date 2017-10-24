package com.horeca.site.controllers;

import com.horeca.site.models.hotel.Link;
import com.horeca.site.services.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
public class LinkController {

    @Autowired
    private LinkService service;

    @RequestMapping(value = "/{hotelId}/links", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Link> getAll(@PathVariable("hotelId") Long hotelId) {
        return service.getAll(hotelId);
    }

    @RequestMapping(value = "/{hotelId}/links/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Link get(@PathVariable("hotelId") Long hotelId, @PathVariable("id") Long id) {
        return service.get(hotelId, id);
    }

    @RequestMapping(value = "/{hotelId}/links", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Link add(@PathVariable("hotelId") Long hotelId, @RequestBody Link link) {
        return service.save(hotelId, link);
    }

    @RequestMapping(value = "/{hotelId}/links/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public Link update(@PathVariable("hotelId") Long hotelId, @PathVariable("id") Long id, @RequestBody Link link) {
        link.setId(id);
        return service.save(hotelId, link);
    }

    @RequestMapping(value = "/{hotelId}/links/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable("hotelId") Long hotelId, @PathVariable("id") Long id) {
        service.delete(hotelId, id);
    }
}
