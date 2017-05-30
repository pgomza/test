package com.horeca.site.models.hotel.services;

public enum AvailableServiceType {

    BREAKFAST("breakfast"), CARPARK("car park"), ROOMSERVICE("room service"), SPA("spa"), PETCARE("pet care"),
    TAXI("taxi"), HOUSEKEEPING("housekeeping"), TABLEORDERING("table ordering"), BAR("bar"), SPACALL("spa call"),
    HAIRDRESSER("hair dresser");

    private final String name;

    AvailableServiceType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
