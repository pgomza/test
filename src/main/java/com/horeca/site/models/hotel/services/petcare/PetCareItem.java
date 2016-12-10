package com.horeca.site.models.hotel.services.petcare;

import com.horeca.site.models.Price;
import com.horeca.site.models.hotel.services.petcare.calendar.PetCareCalendar;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(indexes = @Index(name = "pet_care_id", columnList = "pet_care_id"))
public class PetCareItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String name;

    @NotNull
    private Price price;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn
    private PetCareCalendar calendar;

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

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public PetCareCalendar getCalendar() {
        return calendar;
    }

    public void setCalendar(PetCareCalendar calendar) {
        this.calendar = calendar;
    }
}
