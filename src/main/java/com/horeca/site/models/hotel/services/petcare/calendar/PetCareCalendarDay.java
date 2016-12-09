package com.horeca.site.models.hotel.services.petcare.calendar;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.joda.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(indexes = @Index(name = "pet_care_calendar_id", columnList = "pet_care_calendar_id"))
public class PetCareCalendarDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate day;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "pet_care_calendar_day_id")
    private Set<PetCareCalendarHour> hours;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public Set<PetCareCalendarHour> getHours() {
        return hours;
    }

    public void setHours(Set<PetCareCalendarHour> hours) {
        this.hours = hours;
    }
}
