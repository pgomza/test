package com.horeca.site.models.hotel.services.petcare;

import com.horeca.site.models.Translation;

import javax.persistence.Entity;

@Entity
public class PetCareTranslation extends Translation {

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
