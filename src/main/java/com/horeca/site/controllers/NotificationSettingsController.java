package com.horeca.site.controllers;

import com.horeca.site.models.notifications.NotificationSettings;
import com.horeca.site.services.NotificationSettingsService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Api(value = "hotels")
@RestController
@RequestMapping("/api/hotels")
public class NotificationSettingsController {

    @Autowired
    private NotificationSettingsService service;

    @RequestMapping(value = "/{hotelId}/notifications/settings", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public NotificationSettings get(@PathVariable("hotelId") Long hotelId) {
        return service.get(hotelId);
    }

    @RequestMapping(value = "/{hotelId}/notifications/settings", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public NotificationSettings get(@PathVariable("hotelId") Long hotelId,
                                    @RequestBody NotificationSettings settings) {
        return service.update(hotelId, settings);
    }
}
