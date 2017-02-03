package com.horeca.site.controllers;

import com.horeca.site.models.guest.Guest;
import com.horeca.site.services.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/guests")
public class GuestController {

    @Autowired
    private GuestService service;

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<Guest> getAll() {
        return service.getAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Guest get(@PathVariable("id") Long id) {
        return service.get(id);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Guest add(@RequestBody Guest guest) {
        return service.save(guest);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public Guest update(@PathVariable("id") Long id, @RequestBody Guest guest) {
        guest.setId(id);
        return service.save(guest);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }
}
