package com.horeca.site.controllers;

import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.hotel.HotelView;
import com.horeca.site.models.hotel.translation.LanguageCode;
import com.horeca.site.services.HotelService;
import com.horeca.site.services.translation.HotelTranslationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "hotels")
@RestController
@RequestMapping("/api/hotels")
public class HotelController {

	@Autowired
	private HotelService service;

	@Autowired
	private HotelTranslationService hotelTranslationService;

	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<Hotel> getAll(Pageable pageable, LanguageCode languageCode) {
        Page<Hotel> hotelPage = service.getAll(pageable);
        return hotelTranslationService.translateHotelPage(hotelPage, languageCode);
	}

    @RequestMapping(value = "", params = "simplified", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<HotelView> getAllViews(Pageable pageable, LanguageCode languageCode) {
        Page<HotelView> hotelViewPage = service.getAllViews(pageable);
        return hotelTranslationService.translateHotelViewPage(hotelViewPage, languageCode);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Hotel get(@PathVariable("id") Long id, LanguageCode languageCode) {
		Hotel hotel = service.getIfNotMarkedAsDeleted(id);
		return hotelTranslationService.translate(hotel, hotel.getId(), languageCode);
	}

	@RequestMapping(value = "/{id}", params = "simplified", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public HotelView getView(@PathVariable("id") Long id, LanguageCode languageCode) {
		HotelView hotelView = service.getViewIfNotMarkedAsDeleted(id);
		return hotelTranslationService.translate(hotelView, hotelView.getId(), languageCode);
	}

	@RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Hotel> add(@RequestBody Hotel hotel) {
		Hotel added = service.add(hotel);
        return new ResponseEntity<>(added, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Hotel> update(@PathVariable("id") Long id, @RequestBody Hotel hotel) {
        Hotel changed = service.updateFromController(id, hotel);
		return new ResponseEntity<>(changed, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/tv-channels", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> getTvChannels(@PathVariable("id") Long id, LanguageCode languageCode) {
		List<String> tvChannels = service.getTVChannels(id);
		return hotelTranslationService.translate(tvChannels, id, languageCode);
	}

	@RequestMapping(value = "/{id}/tv-channels", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> updateTvChannels(@PathVariable("id") Long id, @RequestBody List<String> tvChannels) {
		return service.updateTVChannels(id, tvChannels);
	}

	@RequestMapping(value = "/{id}/reset", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void reset(@PathVariable("id") Long id) {
		service.reset(id);
	}

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable("id") Long id) {
        service.markAsDeleted(id);
    }

    @ApiOperation(value = "hotels", hidden = true)
	@RequestMapping(value = "/restoration", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void restore(@RequestBody Long id) {
		service.restore(id);
	}

	@RequestMapping(value = "", params = "name", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<Hotel> getByName(@RequestParam("name") String name, Pageable pageable, LanguageCode languageCode) {
        Page<Hotel> hotelPage = service.getByName(name, pageable);
        return hotelTranslationService.translateHotelPage(hotelPage, languageCode);
	}

	@RequestMapping(value = "", params = { "name", "simplified" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<HotelView> getViewsByName(@RequestParam("name") String name, Pageable pageable, LanguageCode languageCode) {
        Page<HotelView> hotelViewPage = service.getViewsByName(name, pageable);
        return hotelTranslationService.translateHotelViewPage(hotelViewPage, languageCode);
	}

	@RequestMapping(value = "", params = "city", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<Hotel> getByCity(@RequestParam("city") String city, Pageable pageable, LanguageCode languageCode) {
        Page<Hotel> hotelPage = service.getByCity(city, pageable);
        return hotelTranslationService.translateHotelPage(hotelPage, languageCode);
    }

	@RequestMapping(value = "", params = { "city", "simplified" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<HotelView> getViewsByCity(@RequestParam("city") String city, Pageable pageable, LanguageCode languageCode) {
        Page<HotelView> hotelViewPage = service.getViewsByCity(city, pageable);
        return hotelTranslationService.translateHotelViewPage(hotelViewPage, languageCode);
    }

	@RequestMapping(value = "", params = { "name", "city" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<Hotel> getByNameAndCity(@RequestParam("name") String name,
										@RequestParam("city") String city,
										Pageable pageable,
                                        LanguageCode languageCode) {
        Page<Hotel> hotelPage = service.getByNameAndCity(name, city, pageable);
        return hotelTranslationService.translateHotelPage(hotelPage, languageCode);
    }

	@RequestMapping(value = "", params = { "name", "city", "simplified" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<HotelView> getByViewsNameAndCity(@RequestParam("name") String name,
												 @RequestParam("city") String city,
													Pageable pageable,
                                                 LanguageCode languageCode) {
        Page<HotelView> hotelViewPage = service.getViewsByNameAndCity(name, city, pageable);
        return hotelTranslationService.translateHotelViewPage(hotelViewPage, languageCode);
    }
}
