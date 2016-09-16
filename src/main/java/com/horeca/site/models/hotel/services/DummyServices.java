package com.horeca.site.models.hotel.services;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
public class DummyServices {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn
    private Set<DummyService> services;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<DummyService> getServices() {
        return services;
    }

    public void setServices(Set<DummyService> services) {
        this.services = services;
    }
}
