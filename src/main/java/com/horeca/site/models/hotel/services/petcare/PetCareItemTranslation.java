package com.horeca.site.models.hotel.services.petcare;

import com.horeca.site.models.Translation;

import javax.persistence.Entity;

@Entity
public class PetCareItemTranslation extends Translation {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
