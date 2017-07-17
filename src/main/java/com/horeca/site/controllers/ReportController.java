package com.horeca.site.controllers;

import com.horeca.site.models.orders.report.Report;
import com.horeca.site.services.ReportGeneratorService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "orders")
@RestController
@RequestMapping("/api/stays")
public class ReportController {

    @Autowired
    private ReportGeneratorService service;

    @RequestMapping(value = "/{pin}/report", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Report get(@PathVariable("pin") String pin) {
        return service.generateReport(pin);
    }
}
