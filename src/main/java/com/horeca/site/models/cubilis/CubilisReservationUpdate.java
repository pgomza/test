package com.horeca.site.models.cubilis;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.joda.time.LocalDate;

import javax.validation.constraints.NotNull;

public class CubilisReservationUpdate {

    @NotNull
    private Long id;

    private Long guestId;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate arrival;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate departure;

    @NotNull
    private CubilisCustomer cubilisCustomer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGuestId() {
        return guestId;
    }

    public void setGuestId(Long guestId) {
        this.guestId = guestId;
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
}
