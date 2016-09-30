package com.horeca.site.models.stay;

import com.horeca.site.models.hotel.HotelView;
import com.horeca.site.models.orders.OrdersView;

public class StayView {

    private String pin;

    private String name;

    private String date;

    private String roomNumber;

    private StayStatus status;

    private OrdersView orders;

    private HotelView hotel;

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
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

    public StayStatus getStatus() {
        return status;
    }

    public void setStatus(StayStatus status) {
        this.status = status;
    }

    public OrdersView getOrders() {
        return orders;
    }

    public void setOrders(OrdersView orders) {
        this.orders = orders;
    }

    public HotelView getHotel() {
        return hotel;
    }

    public void setHotel(HotelView hotel) {
        this.hotel = hotel;
    }
}
