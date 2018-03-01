package com.horeca.site.models.orders;

import com.horeca.site.models.hotel.translation.Translatable;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/*
    at the very minimum, the data of each service's item has to contain the name field
 */
@MappedSuperclass
public abstract class ServiceItemBaseData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Translatable
    @NotEmpty
    private String name;

    protected ServiceItemBaseData() {}

    protected ServiceItemBaseData(String name) {
        this.name = name;
    }

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
}
