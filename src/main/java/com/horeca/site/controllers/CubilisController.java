package com.horeca.site.controllers;

import com.horeca.site.models.cubilis.CubilisConnectionStatusView;
import com.horeca.site.models.cubilis.CubilisReservationUpdate;
import com.horeca.site.models.cubilis.CubilisRoomsPerHotel;
import com.horeca.site.models.cubilis.CubilisSettings;
import com.horeca.site.services.cubilis.CubilisReservationService;
import com.horeca.site.services.cubilis.CubilisService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public CubilisConnectionStatusView getStatus(@PathVariable("hotelId") Long hotelId) {
        return cubilisService.getConnectionStatus(hotelId).toView();
    }

    @RequestMapping(value = "/{hotelId}/cubilis/rooms", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CubilisRoomsPerHotel> getAvailableRooms(@PathVariable("hotelId") Long hotelId) {
        return cubilisService.getAvailableRooms(hotelId);
    }

    @RequestMapping(value = "/{hotelId}/cubilis/reservations", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CubilisReservationUpdate> getReservations(@PathVariable("hotelId") Long hotelId) {
        return cubilisReservationService.getAllViews(hotelId);
    }

    @RequestMapping(value = "/{hotelId}/cubilis/reservations/{id}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public CubilisReservationUpdate updateReservation(@PathVariable("hotelId") Long hotelId,
                                                      @PathVariable("id") Long id,
                                                      @Valid @RequestBody CubilisReservationUpdate updated) {
        return cubilisReservationService.update(hotelId, id, updated);
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
