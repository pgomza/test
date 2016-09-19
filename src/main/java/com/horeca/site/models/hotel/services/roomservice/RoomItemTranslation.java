package com.horeca.site.models.hotel.services.roomservice;

import com.horeca.site.models.Translation;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;

@Entity
public class RoomItemTranslation extends Translation {

    @NotEmpty
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
