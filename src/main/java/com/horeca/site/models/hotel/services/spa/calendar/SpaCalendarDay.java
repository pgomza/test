package com.horeca.site.models.hotel.services.spa.calendar;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
public class SpaCalendarDay {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @NotNull
    private String day; //TODO change the type

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn
    private Set<SpaCalendarHour> hours;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Set<SpaCalendarHour> getHours() {
        return hours;
    }

    public void setHours(Set<SpaCalendarHour> hours) {
        this.hours = hours;
    }
}
