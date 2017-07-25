package com.horeca.site.models.cubilis;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

public class CubilisRoomsPerHotel {

    @NotNull
    private Long hotelId;

    @NotEmpty
    private List<CubilisRoom> rooms;

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public List<CubilisRoom> getRooms() {
        return rooms;
    }

    public void setRooms(List<CubilisRoom> rooms) {
        this.rooms = rooms;
    }
}
