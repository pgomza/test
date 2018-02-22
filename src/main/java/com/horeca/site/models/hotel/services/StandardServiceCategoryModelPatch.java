package com.horeca.site.models.hotel.services;

import org.hibernate.validator.constraints.NotEmpty;

public class StandardServiceCategoryModelPatch {

    @NotEmpty
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
