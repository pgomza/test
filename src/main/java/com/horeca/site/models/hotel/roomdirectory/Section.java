package com.horeca.site.models.hotel.roomdirectory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.horeca.site.models.Translatable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
public class Section extends Translatable<SectionTranslation> {

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
}
