package com.horeca.site.models.hotel.services.roomservice;

import com.horeca.site.models.Translation;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;

@Entity
public class RoomServiceTranslation extends Translation {

    @NotEmpty
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
