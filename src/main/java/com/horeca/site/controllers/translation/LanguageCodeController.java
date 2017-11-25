package com.horeca.site.controllers.translation;

import com.horeca.site.models.hotel.translation.LanguageCode;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LanguageCodeController {

    @RequestMapping(value = "/languages", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<LanguageInfo> getAll() {
        LanguageCode[] values = LanguageCode.values();
        return Arrays.stream(values)
                .map(code -> new LanguageInfo(code.name(), code.getFullName()))
                .collect(Collectors.toList());
    }

    public static class LanguageInfo {
        public String code;
        public String name;

        LanguageInfo() {}

        public LanguageInfo(String code, String name) {
            this.code = code;
            this.name = name;
        }
    }
}
