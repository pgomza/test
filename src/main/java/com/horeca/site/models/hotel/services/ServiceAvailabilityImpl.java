package com.horeca.site.models.hotel.services;

import javax.validation.constraints.NotNull;

public class ServiceAvailabilityImpl {

    @NotNull
    private Boolean available;

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
