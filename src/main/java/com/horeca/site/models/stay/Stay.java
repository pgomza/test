package com.horeca.site.models.stay;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.horeca.site.models.guest.Guest;
import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.orders.Orders;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Stay {

    @Id
    private String pin;

    private String fromDate; //TODO change the type

    private String toDate; //TODO change the type

    @NotNull
    private String roomNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StayStatus status = StayStatus.NEW;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn
    private Orders orders = new Orders();

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Hotel hotel;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Guest guest;

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public StayStatus getStatus() {
        return status;
    }

    public void setStatus(StayStatus status) {
        this.status = status;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
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
}
