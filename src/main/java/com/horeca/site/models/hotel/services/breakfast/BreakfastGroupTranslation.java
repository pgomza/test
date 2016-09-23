package com.horeca.site.models.hotel.services.breakfast;

import com.horeca.site.models.Translation;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class BreakfastGroupTranslation extends Translation {

    @NotNull
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
