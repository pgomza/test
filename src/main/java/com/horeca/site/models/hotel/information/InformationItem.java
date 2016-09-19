package com.horeca.site.models.hotel.information;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.horeca.site.models.CustomDateSerializer;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class InformationItem {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @NotEmpty
    private String name;

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date fromHour;

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date toHour;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getFromHour() {
        return fromHour;
    }

    public void setFromHour(Date fromHour) {
        this.fromHour = fromHour;
    }

    public Date getToHour() {
        return toHour;
    }

    public void setToHour(Date toHour) {
        this.toHour = toHour;
    }
}

