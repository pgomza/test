package com.horeca.site.controllers;

import com.horeca.site.services.StayInfoAsHtmlService;
import com.horeca.site.services.services.HtmlToPdfService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Api(value = "stays")
@RestController
@RequestMapping("/api/stays")
public class StayInfoController {

    @Autowired
    private StayInfoAsHtmlService service;

    @Autowired
    private HtmlToPdfService htmlToPdfService;

    @RequestMapping(value = "/{pin}/info", params = { "pdf" }, method = RequestMethod.GET)
    public ResponseEntity<byte[]> getPdf(@PathVariable("pin") String pin)
            throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/pdf; charset=utf-8");
        headers.set("Content-Disposition", "inline; filename=info.pdf");

        String html = service.generate(pin);
        InputStream htmlStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream pdfStream = htmlToPdfService.convert(htmlStream, 8.27f, 11.69f);

        return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);
    }
}
