package com.horeca.site.controllers.services;

import com.horeca.site.models.hotel.services.roomservice.RoomService;
import com.horeca.site.models.hotel.services.roomservice.RoomServiceItemUpdate;
import com.horeca.site.services.services.RoomServiceService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "hotels")
@RestController
@RequestMapping("/api/hotels")
public class RoomServiceController {

    @Autowired
    private RoomServiceService roomServiceService;

    @RequestMapping(value = "/{hotelId}/services/roomservice", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public RoomService get(@PathVariable("hotelId") Long hotelId) {
        return roomServiceService.get(hotelId);
    }

    @RequestMapping(value = "/{hotelId}/services/roomservice/items", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void addItem(@PathVariable("hotelId") Long hotelId,
                        @Valid @RequestBody RoomServiceItemUpdate item) {
        roomServiceService.addItem(hotelId, item);
    }

    //TODO path variables should not be empty
    @RequestMapping(value = "/{hotelId}/services/roomservice/items/{itemId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateItem(@PathVariable("hotelId") Long hotelId,
                           @PathVariable("itemId") Long itemId,
                           @Valid @RequestBody RoomServiceItemUpdate item) {
        item.setId(itemId);
        roomServiceService.updateItem(hotelId, item);
    }

    //TODO path variables should not be empty
    @RequestMapping(value = "/{hotelId}/services/roomservice/items/{itemId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteItem(@PathVariable("hotelId") Long hotelId,
                           @PathVariable("itemId") Long itemId) {
        roomServiceService.deleteItem(itemId);
    }
}
