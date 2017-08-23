package com.horeca.site.controllers;

import com.horeca.site.models.stay.*;
import com.horeca.site.services.services.StayService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Api(value = "stays")
@RestController
@RequestMapping("/api")
public class StayController {

    @Autowired
    private StayService stayService;

    @RequestMapping(value = "/stays", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<StayView> getAll() {
        return stayService.getAllViews();
    }

    @RequestMapping(value = "/stays", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Stay> add(@Valid @RequestBody StayPOST entity) {
        Stay stay = stayService.registerNewStay(entity);
        URI newPollUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{pin}")
                .buildAndExpand(stay.getPin())
                .toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(newPollUri);
        return new ResponseEntity<>(stay, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/stays/{pin}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public StayView get(@PathVariable String pin) {
        return stayService.getView(pin);
    }

    @RequestMapping(value = "/stays/{pin}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public Stay update(@PathVariable String pin, @RequestBody StayUPDATE entity) {
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
    public StayView checkIn(@PathVariable String pin) {
        return stayService.checkIn(pin);
    }

    @RequestMapping(value = "/check-out/{pin}", method = RequestMethod.POST)
    public void checkOut(@PathVariable String pin) {
        stayService.checkOut(pin);
    }

    @RequestMapping(value = "/stays/{pin}/notification", method = RequestMethod.POST)
    public void sendNotification(@PathVariable String pin) {
        stayService.notifyGuestAboutStay(pin);
    }

    public static class ResponseMessage {
        private String message;

        public ResponseMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
