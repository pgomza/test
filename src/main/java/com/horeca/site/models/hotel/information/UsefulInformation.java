package com.horeca.site.models.hotel.information;

import javax.persistence.*;
import java.util.Set;

@Entity
public class UsefulInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn
    private Set<UsefulInformationHourItem> hours;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn
    private Set<UsefulInformationOtherItem> other;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<UsefulInformationHourItem> getHours() {
        return hours;
    }

    public void setHours(Set<UsefulInformationHourItem> hours) {
        this.hours = hours;
    }

    public Set<UsefulInformationOtherItem> getOther() {
        return other;
    }

    public void setOther(Set<UsefulInformationOtherItem> other) {
        this.other = other;
    }
}
