package com.horeca.site.controllers.services;

import com.horeca.site.models.hotel.services.petcare.PetCare;
import com.horeca.site.models.hotel.services.petcare.PetCareItem;
import com.horeca.site.services.services.PetCareService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(value = "hotels")
@RestController
@RequestMapping("/api/hotels")
public class PetCareController {

    @Autowired
    private PetCareService petCareService;

    @RequestMapping(value = "/{hotelId}/services/petcare", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public PetCare get(@PathVariable("hotelId") Long hotelId) {
        return petCareService.get(hotelId);
    }

    @RequestMapping(value = "/{hotelId}/services/petcare", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public PetCare addDefault(@PathVariable("hotelId") Long hotelId) {
        return petCareService.addDefaultPetCare(hotelId);
    }

    @RequestMapping(value = "/{hotelId}/services/petcare/items", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PetCareItem> getItems(@PathVariable("hotelId") Long hotelId) {
        return petCareService.getItems(hotelId);
    }

    @RequestMapping(value = "/{hotelId}/services/petcare/items/{itemId}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public PetCareItem getItem(@PathVariable("hotelId") Long hotelId, @PathVariable("itemId") Long itemId) {
        return petCareService.getItem(hotelId, itemId);
    }

    @RequestMapping(value = "/{hotelId}/services/petcare/items", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public PetCareItem addItem(@PathVariable("hotelId") Long hotelId, @Valid @RequestBody PetCareItem item) {
        return petCareService.addItem(hotelId, item);
    }

    @RequestMapping(value = "/{hotelId}/services/petcare/items/{itemId}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public PetCareItem updateItem(@PathVariable("hotelId") Long hotelId,
                                  @PathVariable("itemId") Long itemId,
                                  @Valid @RequestBody PetCareItem item) {
        item.setId(itemId);
        return petCareService.updateItem(hotelId, item);
    }

    @RequestMapping(value = "/{hotelId}/services/petcare/items/{itemId}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteItem(@PathVariable("hotelId") Long hotelId,
                                  @PathVariable("itemId") Long itemId) {
        petCareService.deleteItem(hotelId, itemId);
    }
}
