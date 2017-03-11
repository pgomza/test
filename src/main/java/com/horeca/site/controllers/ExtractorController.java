package com.horeca.site.controllers;

import com.horeca.site.extractors.HotelDataExtractor;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "stays", hidden = true)
@RestController
@RequestMapping("/api")
public class ExtractorController {

    @Autowired
    private HotelDataExtractor extractor;

    @RequestMapping(value = "/extractor", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public void extract(@RequestParam(value = "size", required = true) Integer size) {
        extractor.extract(size);
    }
}
