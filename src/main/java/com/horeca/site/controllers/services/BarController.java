package com.horeca.site.controllers.services;

import com.horeca.site.models.hotel.services.bar.Bar;
import com.horeca.site.models.hotel.services.bar.BarItem;
import com.horeca.site.models.hotel.services.bar.BarItemUpdate;
import com.horeca.site.services.services.BarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Service
@Transactional
public class BarController {

    @Autowired
    private BarService service;

    @RequestMapping(value = "/{hotelId}/services/bar", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Bar get(@PathVariable("hotelId") Long hotelId) {
        return service.get(hotelId);
    }

    @RequestMapping(value = "/{hotelId}/services/bar", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Bar addDefault(@PathVariable("hotelId") Long hotelId) {
        return service.addDefaultBar(hotelId);
    }

    @RequestMapping(value = "/{hotelId}/services/bar/items", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void addItem(@PathVariable("hotelId") Long hotelId,
                        @Valid @RequestBody BarItemUpdate item) {
        service.addItem(hotelId, item);
    }

    //TODO path variables should not be empty
    @RequestMapping(value = "/{hotelId}/services/bar/items/{itemId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public BarItem updateItem(@PathVariable("hotelId") Long hotelId,
                           @PathVariable("itemId") Long itemId,
                           @Valid @RequestBody BarItemUpdate item) {
        item.setId(itemId);
        return service.updateItem(hotelId, item);
    }

    //TODO path variables should not be empty
    @RequestMapping(value = "/{hotelId}/services/bar/items/{itemId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteItem(@PathVariable("hotelId") Long hotelId,
                           @PathVariable("itemId") Long itemId) {
        service.deleteItem(hotelId, itemId);
    }
}
