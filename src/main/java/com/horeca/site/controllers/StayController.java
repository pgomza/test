package com.horeca.site.controllers;

import com.horeca.site.models.Stay;
import com.horeca.site.services.StayService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "stays")
@RestController
@RequestMapping("/api")
public class StayController {

    @Autowired
    private StayService stayService;

    @RequestMapping(value = "/stays", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<Stay> getAll() {
        return stayService.getAll();
    }

    @RequestMapping(value = "/stays", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Stay add(@Valid @RequestBody Stay entity) {
        return stayService.registerNewStay(entity);
    }

    @RequestMapping(value = "/stays/{pin}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Stay get(@PathVariable String pin) {
        return stayService.get(pin);
    }

    @RequestMapping(value = "/stays/{pin}", method = RequestMethod.PUT)
    public Stay update(@PathVariable String pin, @Valid @RequestBody Stay entity) {
        return stayService.update(pin, entity);
    }

    @RequestMapping(value = "/stays/{pin}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable String pin) {
        stayService.delete(pin);
    }

    @RequestMapping(value = "/check-in/{pin}", method = RequestMethod.POST,
            consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Stay checkIn(@PathVariable String pin) {
        return stayService.checkIn(pin);
    }

    @RequestMapping(value = "/check-out/{pin}", method = RequestMethod.POST,
            consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void checkOut(@PathVariable String pin) {
        stayService.checkOut(pin);
    }
}
