package com.horeca.site.models.hotel.services.spa;

import com.horeca.site.models.Translation;

import javax.persistence.Entity;

@Entity
public class SpaItemTranslation extends Translation {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
