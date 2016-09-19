package com.horeca.site.models;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Currency {
    ZLOTY("PLN"),
    EURO("€"),
    DOLLAR("$");

    private final String name;

    private Currency(String name) {
        this.name  = name;
    }

    @Override
    @JsonValue
    public String toString() {
        return this.name;
    }
}
