package com.horeca.site.controllers;

import com.horeca.site.models.orders.report.Report;
import com.horeca.site.services.ReportGeneratorService;
import com.horeca.site.services.services.HtmlToPdfService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Api(value = "orders")
@RestController
@RequestMapping("/api/stays")
public class ReportController {

    @Autowired
    private ReportGeneratorService reportGeneratorService;

    @Autowired
    private HtmlToPdfService htmlToPdfService;

    @RequestMapping(value = "/{pin}/report", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Report get(@PathVariable("pin") String pin) {
        return reportGeneratorService.generateReport(pin);
    }

    @RequestMapping(value = "/{pin}/report", params = { "pdf"}, method = RequestMethod.GET)
    public ResponseEntity<byte[]> getPdf(@PathVariable("pin") String pin, @RequestParam(name = "pdf") Boolean isPdf)
            throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/pdf; charset=utf-8");
        headers.set("Content-Disposition", "inline; filename=report.pdf");

        String html = reportGeneratorService.generateReportInHtml(pin);
        InputStream htmlStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream pdfStream = htmlToPdfService.convert(htmlStream, 11.69f, 8.27f);

        return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);
    }
}
