package com.horeca.site.models.stay;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.horeca.site.models.Viewable;
import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.orders.Orders;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Stay implements Viewable<StayView> {

    @Id
    private String pin;

    @NotNull
    private String name;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String date; //TODO change to the proper type

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

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getPin() {
        return pin;
    }

    public StayStatus getStatus() {
        return status;
    }

    public void setStatus(StayStatus status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
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

    @Override
    public StayView toView(String preferredLanguage, String defaultLanguage) {
        StayView view = new StayView();
        view.setPin(getPin());
        view.setName(getName());
        view.setDate(getDate());
        view.setRoomNumber(getRoomNumber());
        view.setStatus(getStatus());
        view.setOrders(getOrders().toView(preferredLanguage, defaultLanguage));
        view.setHotel(getHotel().toView(preferredLanguage, defaultLanguage));
        return view;
    }
}
