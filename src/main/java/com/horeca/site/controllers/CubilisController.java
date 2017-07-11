package com.horeca.site.controllers;

import com.horeca.site.models.cubilis.CubilisConnectionStatus;
import com.horeca.site.models.cubilis.CubilisReservation;
import com.horeca.site.models.cubilis.CubilisSettings;
import com.horeca.site.services.CubilisReservationService;
import com.horeca.site.services.CubilisService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "hotels")
@RestController
@RequestMapping("/api/hotels")
public class CubilisController {

    @Autowired
    private CubilisService cubilisService;

    @Autowired
    private CubilisReservationService cubilisReservationService;

    @RequestMapping(value = "/{hotelId}/cubilis/settings", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public CubilisSettings getSettings(@PathVariable("hotelId") Long hotelId) {
        return cubilisService.getSettings(hotelId);
    }

    @RequestMapping(value = "/{hotelId}/cubilis/settings", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public CubilisSettings updateSettings(@PathVariable("hotelId") Long hotelId, @RequestBody CubilisSettings settings) {
        return cubilisService.updateSettings(hotelId, settings);
    }

    @RequestMapping(value = "/{hotelId}/cubilis/status", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public CubilisConnectionStatus getStatus(@PathVariable("hotelId") Long hotelId) {
        return cubilisService.getConnectionStatus(hotelId);
    }

    @RequestMapping(value = "/{hotelId}/cubilis/reservations", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CubilisReservation> getReservations(@PathVariable("hotelId") Long hotelId) {
        return cubilisReservationService.getAll(hotelId);
    }

    @RequestMapping(value = "/{hotelId}/cubilis/confirmation", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void confirmReservations(@PathVariable("hotelId") Long hotelId, @RequestBody List<Long> reservationIds) {
        cubilisReservationService.confirm(hotelId, reservationIds);
    }

    @RequestMapping(value = "/{hotelId}/cubilis/rejection", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void rejectReservations(@PathVariable("hotelId") Long hotelId, @RequestBody List<Long> reservationIds) {
        cubilisReservationService.reject(hotelId, reservationIds);
    }
}
