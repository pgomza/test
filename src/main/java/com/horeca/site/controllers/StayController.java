package com.horeca.site.controllers;

import com.horeca.annotations.AllowCORS;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.models.stay.StayPOST;
import com.horeca.site.models.stay.StayStatusUPDATE;
import com.horeca.site.models.stay.StayView;
import com.horeca.site.services.services.StayService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Api(value = "stays")
@AllowCORS
@RestController
@RequestMapping("/api")
public class StayController {

    @Autowired
    private StayService stayService;

    @RequestMapping(value = "/stays", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<StayView> getAll(HttpServletRequest request) {
        String preferredLanguage = request.getLocale().getLanguage();
        return stayService.getAllViews(preferredLanguage);
    }

    @RequestMapping(value = "/stays", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Stay add(@Valid @RequestBody StayPOST entity) {
        return stayService.registerNewStay(entity);
    }

    @RequestMapping(value = "/stays/{pin}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public StayView get(@PathVariable String pin, HttpServletRequest request) {
        String preferredLanguage = request.getLocale().getLanguage();
        return stayService.getView(pin, preferredLanguage);
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
    public StayView checkIn(@PathVariable String pin, HttpServletRequest request) {
        String preferredLanguage = request.getLocale().getLanguage();
        Stay stay = stayService.checkIn(pin);
        return stay.toView(preferredLanguage, stay.getHotel().getDefaultTranslation());
    }

    @RequestMapping(value = "/check-out/{pin}", method = RequestMethod.POST)
    public void checkOut(@PathVariable String pin) {
        stayService.checkOut(pin);
    }
}
