package com.horeca.site.models.hotel.information;

import com.horeca.site.models.hotel.translation.Translatable;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Set;

@Entity
@Audited
public class UsefulInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Translatable
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn
    private Set<UsefulInformationHourItem> hours;

    @Translatable
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
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
