package com.horeca.site.controllers;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.ShutdownEndpoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Api(value = "hotels", hidden = true)
@RestController
@RequestMapping("/api/maintenance")
public class ShutdownController {

    @Autowired
    private ShutdownEndpoint shutdownEndpoint;

    @Value("${shutdownSecret}")
    private String shutdownSecret;

    @RequestMapping(value = "/shutdown", method = RequestMethod.GET)
    public ResponseEntity<Void> shutdown(@RequestParam(value = "secret", required = true) String secret) {
        if (Objects.equals(secret, shutdownSecret)) {
            shutdownEndpoint.invoke();
            // the application context is actually closed in a different thread with some delay (by default 500 ms)
            // so the correct status will be returned before the context closes
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
