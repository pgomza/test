package com.horeca.site.models.hotel.services.spa.calendar;

import javax.persistence.*;
import java.util.Set;

@Entity
public class SpaCalendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "spa_calendar_id")
    private Set<SpaCalendarDay> days;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<SpaCalendarDay> getDays() {
        return days;
    }

    public void setDays(Set<SpaCalendarDay> days) {
        this.days = days;
    }
}
