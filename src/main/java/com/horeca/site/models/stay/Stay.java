package com.horeca.site.models.stay;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.horeca.site.models.guest.Guest;
import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.orders.Orders;
import org.joda.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Stay {

    @Id
    private String pin;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate fromDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate toDate;

    @NotNull
    private String roomNumber;

    private String wifiPassword;

    private String doorKey;

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

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getWifiPassword() {
        return wifiPassword;
    }

    public void setWifiPassword(String wifiPassword) {
        this.wifiPassword = wifiPassword;
    }

    public String getDoorKey() {
        return doorKey;
    }

    public void setDoorKey(String doorKey) {
        this.doorKey = doorKey;
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

    public StayView toView() {
        StayView view = new StayView();

        view.setPin(getPin());
        view.setFromDate(getFromDate());
        view.setToDate(getToDate());
        view.setRoomNumber(getRoomNumber());
        view.setWifiPassword(getWifiPassword());
        view.setDoorKey(getDoorKey());
        view.setStatus(getStatus());
        view.setOrders(getOrders());
        view.setHotel(getHotel().toView());
        view.setGuest(getGuest());

        return view;
    }
}
