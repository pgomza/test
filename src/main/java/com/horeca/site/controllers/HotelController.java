package com.horeca.site.controllers;

import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.hotel.HotelView;
import com.horeca.site.services.HotelService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "hotels")
@RestController
@RequestMapping("/api/hotels")
public class HotelController {

	@Autowired
	private HotelService service;

	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<Hotel> getAll(Pageable pageable) {
		return service.getAll(pageable);
	}

    @RequestMapping(value = "", params = "simplified", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<HotelView> getAllViews(Pageable pageable) {
        return service.getAllViews(pageable);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Hotel get(@PathVariable("id") Long id) {
		return service.get(id);
	}

	@RequestMapping(value = "/{id}", params = "simplified", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public HotelView getView(@PathVariable("id") Long id) {
		return service.getView(id);
	}

	@RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Hotel> add(@RequestBody Hotel hotel) {
		Hotel added = service.add(hotel);
        return new ResponseEntity<>(added, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Hotel> update(@PathVariable("id") Long id, @Valid @RequestBody Hotel hotel) {
        Hotel changed = service.updateFromController(id, hotel);
		return new ResponseEntity<Hotel>(changed, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/reset", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void reset(@PathVariable("id") Long id) {
		service.reset(id);
	}

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable("id") Long id) {
        service.markAsDeleted(id);
    }

	@RequestMapping(value = "", params = "name", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<Hotel> getByName(@RequestParam("name") String name, Pageable pageable) {
		return service.getByName(name, pageable);
	}

	@RequestMapping(value = "", params = { "name", "simplified" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<HotelView> getViewsByName(@RequestParam("name") String name, Pageable pageable) {
		return service.getViewsByName(name, pageable);
	}

	@RequestMapping(value = "", params = "city", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<Hotel> getByCity(@RequestParam("city") String city, Pageable pageable) {
		return service.getByCity(city, pageable);
	}

	@RequestMapping(value = "", params = { "city", "simplified" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<HotelView> getViewsByCity(@RequestParam("city") String city, Pageable pageable) {
		return service.getViewsByCity(city, pageable);
	}

	@RequestMapping(value = "", params = { "name", "city" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<Hotel> getByNameAndCity(@RequestParam("name") String name,
										@RequestParam("city") String city,
										Pageable pageable) {
		return service.getByNameAndCity(name, city, pageable);
	}

	@RequestMapping(value = "", params = { "name", "city", "simplified" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<HotelView> getByViewsNameAndCity(@RequestParam("name") String name,
												 @RequestParam("city") String city,
													Pageable pageable) {
		return service.getViewsByNameAndCity(name, city, pageable);
	}
}
