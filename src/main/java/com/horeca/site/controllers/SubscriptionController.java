package com.horeca.site.controllers;

import com.horeca.site.models.hotel.subscription.SubscriptionEvent;
import com.horeca.site.models.hotel.subscription.SubscriptionEventView;
import com.horeca.site.models.hotel.subscription.SubscriptionView;
import com.horeca.site.services.subscription.SubscriptionService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Api(value = "hotels")
@RestController
@RequestMapping("/api/hotels")
public class SubscriptionController {

    @Autowired
    private SubscriptionService service;

    @RequestMapping(value = "/{hotelId}/subscription", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public SubscriptionView get(@PathVariable("hotelId") Long hotelId) {
        return service.getView(hotelId);
    }

    @RequestMapping(value = "/{hotelId}/subscription/trial", method = RequestMethod.POST, produces = MediaType
            .APPLICATION_JSON_VALUE)
    public SubscriptionEvent enableTrial(@PathVariable("hotelId") Long hotelId) {
        return service.enableTrial(hotelId);
    }

    @RequestMapping(value = "/{hotelId}/subscription/events", method = RequestMethod.GET, produces = MediaType
            .APPLICATION_JSON_VALUE)
    public Page<SubscriptionEventView> getHistory(Pageable pageable, @PathVariable("hotelId") Long hotelId) {
        return service.getHistoryView(hotelId, pageable);
    }

    /*
        for testing purposes only
     */
    @RequestMapping(value = "/{hotelId}/subscription/events", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public SubscriptionEvent get(@PathVariable("hotelId") Long hotelId, @RequestBody Integer level) {
        return service.addPremiumEvent(hotelId, level);
    }
}
