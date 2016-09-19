package com.horeca.site.models.hotel.services;

import com.horeca.site.models.hotel.services.breakfast.BreakfastView;
import com.horeca.site.models.hotel.services.carpark.CarParkView;
import com.horeca.site.models.hotel.services.receptioncall.ReceptionCall;
import com.horeca.site.models.hotel.services.roomservice.RoomServiceView;

public class AvailableServicesView {

    private Long id;

    private BreakfastView breakfast;

    private CarParkView carPark;

    private RoomServiceView roomService;

    private ReceptionCall receptionCall;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BreakfastView getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(BreakfastView breakfast) {
        this.breakfast = breakfast;
    }

    public CarParkView getCarPark() {
        return carPark;
    }

    public void setCarPark(CarParkView carPark) {
        this.carPark = carPark;
    }

    public RoomServiceView getRoomService() {
        return roomService;
    }

    public void setRoomService(RoomServiceView roomService) {
        this.roomService = roomService;
    }

    public ReceptionCall getReceptionCall() {
        return receptionCall;
    }

    public void setReceptionCall(ReceptionCall receptionCall) {
        this.receptionCall = receptionCall;
    }
}
