package com.horeca.site.controllers;

import com.horeca.site.models.Stay;
import com.horeca.site.repositories.StayRepository;
import com.horeca.site.services.StayRegistrationService;
import com.horeca.site.validation.ResourceNotFoundException;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "stays")
@RestController
@RequestMapping("/api/stays")
public class StayController {

    @Autowired
    private StayRepository repository;

    @Autowired
    private StayRegistrationService registrationService;

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<Stay> getAll() {
        return repository.findAll();
    }

    @RequestMapping(value = "/{pin}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("authentication.userAuthentication.details['pin'] == #pin")
    public Stay get(@PathVariable String pin) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        checkIfExists(pin);
        return repository.findOne(pin);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Stay add(@Valid @RequestBody Stay entity) {
        return registrationService.registerNewStay(entity);
    }

    @RequestMapping(value = "/{pin}", method = RequestMethod.PUT)
    @PreAuthorize("authentication.userAuthentication.details['pin'] == #pin")
    public Stay update(@Valid @RequestBody Stay entity, @PathVariable String pin) {
        checkIfExists(pin);
        entity.setPin(pin);
        return repository.save(entity);
    }

    @RequestMapping(value = "/{pin}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("authentication.userAuthentication.details['pin'] == #pin")
    public void delete(@PathVariable String pin) {
        checkIfExists(pin);
        repository.delete(pin);
    }

    private void checkIfExists(String pin) {
        boolean exists = repository.exists(pin);
        if (!exists)
            throw new ResourceNotFoundException("Such Stay does not exist");
    }
}
