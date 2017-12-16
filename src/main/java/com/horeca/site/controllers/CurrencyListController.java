package com.horeca.site.controllers;

import com.horeca.site.models.Currency;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CurrencyListController {

    @RequestMapping(value = "/currencies", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CurrencyInfo> getAll() {
        return Arrays.stream(Currency.values())
                .map(c -> new CurrencyInfo(c.getName(), c.getCode(), c.getSymbol()))
                .collect(Collectors.toList());
    }

    public static class CurrencyInfo {
        public String name;
        public String code;
        public String symbol;

        CurrencyInfo() {}

        public CurrencyInfo(String name, String code, String symbol) {
            this.name = name;
            this.code = code;
            this.symbol = symbol;
        }
    }
}
