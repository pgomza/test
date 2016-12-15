package com.horeca.site.controllers;

import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.hotel.HotelView;
import com.horeca.site.services.HotelService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(value = "hotels")
@RestController
@RequestMapping("/api/hotels")
public class HotelController {

	@Autowired
	private HotelService service;

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<HotelView> getAll() {
        return service.getAllViews();
	}

	@RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Hotel> add(@Valid @RequestBody Hotel hotel) {
		Hotel added = service.add(hotel);
        return new ResponseEntity<Hotel>(added, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public HotelView get(@PathVariable("id") Long id) {
        return service.getView(id);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Hotel> update(@PathVariable("id") Long id, @Valid @RequestBody Hotel hotel) {
        Hotel changed = service.update(id, hotel);
		return new ResponseEntity<Hotel>(changed, HttpStatus.OK);
	}

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }
}
