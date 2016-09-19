package com.horeca.site.models.hotel.roomdirectory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.horeca.site.models.Translatable;
import com.horeca.site.models.Viewable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
public class Section extends Translatable<SectionTranslation> implements Viewable<SectionView> {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public SectionView toView(String preferredLanguage, String defaultLanguage) {
        SectionView sectionView = new SectionView();
        SectionTranslation translation = getTranslation(preferredLanguage, defaultLanguage);
        sectionView.setHeading(translation.getHeading());
        sectionView.setText(translation.getText());

        return sectionView;
    }
}
