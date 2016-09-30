package com.horeca.site.models.stay;

import javax.validation.constraints.NotNull;

public class StayPOST {

    @NotNull
    private String name;

    @NotNull
    private String roomNumber;

    @NotNull
    private Long hotelId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }
}
