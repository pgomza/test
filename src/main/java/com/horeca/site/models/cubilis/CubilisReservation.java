package com.horeca.site.models.cubilis;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.horeca.site.models.guest.Guest;
import com.horeca.site.models.hotel.Hotel;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class CubilisReservation {

    @Id
    private Long id;

    private boolean isRejected;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @OneToOne
    @JoinColumn(name = "guest_id")
    private Guest guest;

    @NotEmpty
    private String status;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MMddTHH:mm:ss")
    private LocalDateTime arrival;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate departure;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "customer_id")
    private CubilisCustomer customer;

    @NotNull
    private Integer guestCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isRejected() {
        return isRejected;
    }

    public void setRejected(boolean rejected) {
        isRejected = rejected;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getArrival() {
        return arrival;
    }

    public void setArrival(LocalDateTime arrival) {
        this.arrival = arrival;
    }

    public LocalDate getDeparture() {
        return departure;
    }

    public void setDeparture(LocalDate departure) {
        this.departure = departure;
    }

    public CubilisCustomer getCustomer() {
        return customer;
    }

    public void setCustomer(CubilisCustomer customer) {
        this.customer = customer;
    }

    public Integer getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(Integer guestCount) {
        this.guestCount = guestCount;
    }

    public CubilisReservationUpdate toView() {
        CubilisReservationUpdate view = new CubilisReservationUpdate();
        view.setId(getId());
        view.setArrival(getArrival().toLocalDate());
        view.setDeparture(getDeparture());
        view.setCubilisCustomer(getCustomer());
        if (getGuest() != null) {
            view.setGuestId(getGuest().getId());
        }
        view.setGuestCount(getGuestCount());

        return view;
    }
}
