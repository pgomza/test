package com.horeca.site.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Currency {
    EUR("Euro", "EUR", "€"),
    USD("US Dollar", "USD", "$");

    private final String name;
    private final String code;
    private final String symbol;

    Currency(String name, String code, String symbol) {
        this.name = name;
        this.code = code;
        this.symbol = symbol;
    }

    @JsonValue
    String toValue() {
        return symbol;
    }

    @JsonCreator
    Currency fromValue(String value) {
        Currency[] currencies = Currency.values();
        for (Currency currency : currencies) {
            if (currency.symbol.equals(value)) {
                return currency;
            }
        }
        throw new IllegalArgumentException("Unsupported currency symbol");
    }
}
