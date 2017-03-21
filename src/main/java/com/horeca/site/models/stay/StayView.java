package com.horeca.site.models.stay;

import com.horeca.site.models.guest.Guest;
import com.horeca.site.models.hotel.HotelView;
import com.horeca.site.models.orders.Orders;

public class StayView {

    private String pin;
    private String fromDate;
    private String toDate;
    private String roomNumber;
    private StayStatus status;
    private Orders orders;
    private HotelView hotel;
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

    public HotelView getHotel() {
        return hotel;
    }

    public void setHotel(HotelView hotel) {
        this.hotel = hotel;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }
}
