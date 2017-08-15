package com.horeca.site.models.cubilis;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.joda.time.LocalDate;

import javax.validation.constraints.NotNull;

public class CubilisReservationUpdate {

    @NotNull
    private Long id;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate arrival;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate departure;

    @NotNull
    private CubilisCustomer cubilisCustomer;

    @NotNull
    private Integer guestCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getArrival() {
        return arrival;
    }

    public void setArrival(LocalDate arrival) {
        this.arrival = arrival;
    }

    public LocalDate getDeparture() {
        return departure;
    }

    public void setDeparture(LocalDate departure) {
        this.departure = departure;
    }

    public CubilisCustomer getCubilisCustomer() {
        return cubilisCustomer;
    }

    public void setCubilisCustomer(CubilisCustomer cubilisCustomer) {
        this.cubilisCustomer = cubilisCustomer;
    }

    public Integer getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(Integer guestCount) {
        this.guestCount = guestCount;
    }
}
