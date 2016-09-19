package com.horeca.site.models.hotel.roomdirectory;

import com.horeca.site.models.Translation;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;

@Entity
public class SectionTranslation extends Translation {

    @NotEmpty
    private String heading;

    @NotEmpty
    private String text;

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
