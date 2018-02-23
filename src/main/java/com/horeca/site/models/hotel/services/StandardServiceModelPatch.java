package com.horeca.site.models.hotel.services;

public class StandardServiceModelPatch {

    private String description;

    StandardServiceModelPatch() {}

    public StandardServiceModelPatch(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
