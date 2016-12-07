package com.horeca.site.models.hotel.services.petcare.calendar;

import javax.persistence.*;
import java.util.Set;

@Entity
public class PetCareCalendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn
    private Set<PetCareCalendarDay> days;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<PetCareCalendarDay> getDays() {
        return days;
    }

    public void setDays(Set<PetCareCalendarDay> days) {
        this.days = days;
    }
}
