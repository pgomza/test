package com.horeca.site.models.hotel.address;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.horeca.site.models.Translation;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class AddressTranslation implements Translation {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @NotEmpty
    private String language;

    @NotEmpty
    private String city;

    @NotEmpty
    private String street;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
