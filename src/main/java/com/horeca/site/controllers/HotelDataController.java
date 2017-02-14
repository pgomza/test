package com.horeca.site.controllers;

import com.horeca.site.models.hoteldata.HotelDataView;
import com.horeca.site.services.HotelDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hoteldata")
public class HotelDataController {

    @Autowired
    private HotelDataService service;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public HotelDataView get(@PathVariable("id") Long id) {
        return service.get(id);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public HotelDataView getByName(@RequestParam(value = "name", required = false) String name) {
        return service.getByName(name);
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<HotelDataView> getAll(Pageable pageable) {
        return service.getBatch(pageable);
    }
}
