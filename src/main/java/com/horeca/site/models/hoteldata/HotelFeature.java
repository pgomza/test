package com.horeca.site.models.hoteldata;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;

@Entity
@Table(indexes = {  @Index(name = "type", columnList = "type"),
                    @Index(name = "name", columnList = "name")
})
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class HotelFeature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
