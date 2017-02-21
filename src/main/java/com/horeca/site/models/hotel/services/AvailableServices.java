package com.horeca.site.models.hotel.services;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.horeca.site.models.hotel.services.breakfast.Breakfast;
import com.horeca.site.models.hotel.services.carpark.CarPark;
import com.horeca.site.models.hotel.services.housekeeping.Housekeeping;
import com.horeca.site.models.hotel.services.petcare.PetCare;
import com.horeca.site.models.hotel.services.receptioncall.ReceptionCall;
import com.horeca.site.models.hotel.services.roomservice.RoomService;
import com.horeca.site.models.hotel.services.spa.Spa;
import com.horeca.site.models.hotel.services.taxi.Taxi;

import javax.persistence.*;

@Entity
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class AvailableServices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn
    private Breakfast breakfast;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn
    private CarPark carPark;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn
    private Housekeeping housekeeping;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn
    private ReceptionCall receptionCall;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn
    private Spa spa;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn
    private PetCare petCare;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn
    private Taxi taxi;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn
    private RoomService roomService;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Breakfast getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(Breakfast breakfast) {
        this.breakfast = breakfast;
    }

    public CarPark getCarPark() {
        return carPark;
    }

    public void setCarPark(CarPark carPark) {
        this.carPark = carPark;
    }

    public Housekeeping getHousekeeping() {
        return housekeeping;
    }

    public void setHousekeeping(Housekeeping housekeeping) {
        this.housekeeping = housekeeping;
    }

    public ReceptionCall getReceptionCall() {
        return receptionCall;
    }

    public void setReceptionCall(ReceptionCall receptionCall) {
        this.receptionCall = receptionCall;
    }

    public Spa getSpa() {
        return spa;
    }

    public void setSpa(Spa spa) {
        this.spa = spa;
    }

    public PetCare getPetCare() {
        return petCare;
    }

    public void setPetCare(PetCare petCare) {
        this.petCare = petCare;
    }

    public Taxi getTaxi() {
        return taxi;
    }

    public void setTaxi(Taxi taxi) {
        this.taxi = taxi;
    }

    public RoomService getRoomService() {
        return roomService;
    }

    public void setRoomService(RoomService roomService) {
        this.roomService = roomService;
    }
}
