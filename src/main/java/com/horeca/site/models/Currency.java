package com.horeca.site.models;

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
    public String toValue() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getSymbol() {
        return symbol;
    }
}
