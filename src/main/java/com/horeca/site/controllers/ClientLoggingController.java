package com.horeca.site.controllers;

import com.horeca.site.models.logs.ClientLogEntry;
import com.horeca.site.models.logs.ClientLogEntry.Level;
import com.horeca.site.models.logs.ClientLogEntryView;
import com.horeca.site.services.ClientLoggingService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Api(value = "hotels")
@RestController
@RequestMapping("/api/logs")
public class ClientLoggingController {

    @Autowired
    private ClientLoggingService service;

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<ClientLogEntryView> getViewsByLevels(Pageable pageable, @RequestParam(value = "level", required = false)
            List<String> levelStrings) {

        Page<ClientLogEntry> pageOfEntries;
        if (levelStrings != null && !levelStrings.isEmpty()) {
            Set<Level> levels = levelStrings.stream()
                    .map(String::toUpperCase)
                    .map(Level::valueOf)
                    .collect(Collectors.toSet());
            pageOfEntries = service.getEntriesByLevels(pageable, levels);
        }
        else {
            pageOfEntries = service.getEntries(pageable);
        }

        List<ClientLogEntry> entries = pageOfEntries.getContent();
        List<ClientLogEntryView> entryViews = entries.stream()
                .map(ClientLogEntry::toView)
                .collect(Collectors.toList());

        return new PageImpl<>(entryViews, pageable, pageOfEntries.getTotalElements());
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ClientLogEntry add(@RequestBody ClientLogEntry entry) {
        return service.add(entry);
    }
}
