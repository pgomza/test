package com.horeca.site.controllers;

import org.joda.time.Instant;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class GeneralController {

    @RequestMapping(value = "/status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public StatusInfo getStatus() {
        StatusInfo statusInfo = new StatusInfo();
        statusInfo.setTime(new Instant());
        return statusInfo;
    }

    public static class StatusInfo {
        private Instant time;

        public Instant getTime() {
            return time;
        }

        public void setTime(Instant time) {
            this.time = time;
        }
    }
}
