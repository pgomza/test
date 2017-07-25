package com.horeca.site.models.cubilis;

import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

public class CubilisRoom {

    @NotEmpty
    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private List<CubilisRatePlan> ratePlans;

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

    public List<CubilisRatePlan> getRatePlans() {
        return ratePlans;
    }

    public void setRatePlans(List<CubilisRatePlan> ratePlans) {
        this.ratePlans = ratePlans;
    }
}
