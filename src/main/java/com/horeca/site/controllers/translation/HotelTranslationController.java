package com.horeca.site.controllers.translation;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.models.hotel.translation.HotelTranslationView;
import com.horeca.site.models.hotel.translation.LanguageCode;
import com.horeca.site.models.hotel.translation.TranslationEntry;
import com.horeca.site.services.translation.HotelTranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/hotels")
public class HotelTranslationController {

    @Autowired
    private HotelTranslationService service;

    @RequestMapping(value = "/{hotelId}/translations", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<HotelTranslationView> getAll(@PathVariable("hotelId") Long hotelId) {

        return service.getAllViews(hotelId);
    }

    @RequestMapping(value = "/{hotelId}/translations/{languageCode}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public HotelTranslationView get(@PathVariable("hotelId") Long hotelId,
                                    @PathVariable("languageCode") String languageCode) {

        return service.getView(hotelId, languageCodeFromString(languageCode));
    }

    @RequestMapping(value = "/{hotelId}/translations/{languageCode}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public HotelTranslationView update(@PathVariable("hotelId") Long hotelId,
                                    @PathVariable("languageCode") String languageCode,
                                    @RequestBody Set<TranslationEntry> translationEntries) {

        service.update(hotelId, languageCodeFromString(languageCode), translationEntries);
        return service.getView(hotelId, languageCodeFromString(languageCode));
    }

    @RequestMapping(value = "/{hotelId}/translations/{languageCode}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable("hotelId") Long hotelId,
                                       @PathVariable("languageCode") String languageCode) {

        service.delete(hotelId, languageCodeFromString(languageCode));
    }

    @RequestMapping(value = "/{hotelId}/translations/{languageCode}/entries", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public HotelTranslationView merge(@PathVariable("hotelId") Long hotelId,
                                       @PathVariable("languageCode") String languageCode,
                                       @RequestBody TranslationEntry entry) {

        service.merge(hotelId, languageCodeFromString(languageCode), entry);
        return service.getView(hotelId, languageCodeFromString(languageCode));
    }

    private static LanguageCode languageCodeFromString(String value) {
        try {
            return LanguageCode.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessRuleViolationException("Incorrect language code");
        }
    }
}
