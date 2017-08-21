package com.horeca.site.models.hotel.services.spa;

import com.horeca.site.models.Price;
import com.horeca.site.models.hotel.services.spa.calendar.SpaCalendar;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(indexes = @Index(name = "spa_id", columnList = "spa_id"))
@Audited
public class SpaItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String name;

    @NotNull
    private Price price;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn
    private SpaCalendar calendar;

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

    public SpaCalendar getCalendar() {
        return calendar;
    }

    public void setCalendar(SpaCalendar calendar) {
        this.calendar = calendar;
    }
}
