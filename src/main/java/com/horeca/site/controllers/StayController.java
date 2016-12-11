package com.horeca.site.controllers;

import com.horeca.site.models.stay.Stay;
import com.horeca.site.models.stay.StayPOST;
import com.horeca.site.models.stay.StayStatusUPDATE;
import com.horeca.site.services.services.StayService;
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
    public Stay add(@Valid @RequestBody StayPOST entity) {
        return stayService.registerNewStay(entity);
    }

    @RequestMapping(value = "/stays/{pin}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Stay get(@PathVariable String pin) {
        return stayService.get(pin);
    }

    @RequestMapping(value = "/stays/{pin}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public Stay update(@PathVariable String pin, @Valid @RequestBody Stay entity) {
        return stayService.update(pin, entity);
    }

    @RequestMapping(value = "/stays/{pin}/status", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public StayStatusUPDATE updateStatus(@PathVariable String pin, @Valid @RequestBody StayStatusUPDATE newStatus) {
        return stayService.updateStatus(pin, newStatus);
    }

    @RequestMapping(value = "/stays/{pin}", method = RequestMethod.DELETE)
    public void delete(@PathVariable String pin) {
        stayService.delete(pin);
    }

    @RequestMapping(value = "/stays", method = RequestMethod.DELETE)
    public void deleteAll() {
        stayService.deleteAll();
    }

    @RequestMapping(value = "/check-in/{pin}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Stay checkIn(@PathVariable String pin) {
        return stayService.checkIn(pin);
    }

    @RequestMapping(value = "/check-out/{pin}", method = RequestMethod.POST)
    public void checkOut(@PathVariable String pin) {
        stayService.checkOut(pin);
    }
}
