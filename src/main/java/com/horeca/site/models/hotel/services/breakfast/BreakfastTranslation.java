package com.horeca.site.models.hotel.services.breakfast;

import com.horeca.site.models.Translation;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class BreakfastTranslation extends Translation {

    @NotNull
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
