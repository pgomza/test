package com.horeca.site.controllers;

import com.horeca.site.models.StaticTranslation;
import com.horeca.site.services.StaticTranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/static-translations")
public class StaticTranslationController {

    @Autowired
    private StaticTranslationService service;

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<StaticTranslation> getAll() {
        return service.getAll();
    }

    @RequestMapping(value = "/{language}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public StaticTranslation get(@PathVariable("language") String language) {
        return service.get(language);
    }

    @RequestMapping(value = "/{language}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public StaticTranslation update(@PathVariable("language") String language, @RequestBody StaticTranslation translation) {
        translation.setLanguage(language);
        return service.update(translation);
    }

    @RequestMapping(value = "/{language}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable("language") String language) {
        service.delete(language);
    }
}
