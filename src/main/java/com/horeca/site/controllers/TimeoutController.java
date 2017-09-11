package com.horeca.site.controllers;

import com.horeca.site.models.TimeoutSettings;
import com.horeca.site.repositories.TimeoutSettingsRepository;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Api(hidden = true)
public class TimeoutController {

    @Autowired
    private TimeoutSettingsRepository repository;

    @GetMapping("/timeout")
    public TimeoutSettings getSettings() {
        return repository.findOne(1L);
    }

    @PutMapping("/timeout")
    public TimeoutSettings updateTimeoutSettings(@RequestBody TimeoutSettings timeoutSettings) throws InterruptedException {
        TimeoutSettings settings = repository.findOne(1L);
        settings.setEnabled(timeoutSettings.isEnabled());
        return repository.save(settings);
    }
}
