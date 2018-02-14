package com.horeca.site.models.hotel.services.spacall;

import com.horeca.site.models.hotel.services.HotelServiceModel;
import com.horeca.site.models.hotel.translation.Translatable;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Audited
public class SpaCall implements HotelServiceModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Translatable
    private String description;

    @NotEmpty
    private String number;

    @NotNull
    private Boolean available;

    @Override
    public Boolean getAvailable() {
        return available;
    }

    @Override
    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
