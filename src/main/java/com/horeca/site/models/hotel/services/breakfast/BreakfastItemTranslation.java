package com.horeca.site.models.hotel.services.breakfast;

import com.horeca.site.models.Translation;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;

@Entity
public class BreakfastItemTranslation extends Translation {

    @NotEmpty
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
