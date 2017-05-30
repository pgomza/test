package com.horeca.site.models.hotel.services;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.horeca.site.models.hotel.services.bar.Bar;
import com.horeca.site.models.hotel.services.breakfast.Breakfast;
import com.horeca.site.models.hotel.services.carpark.CarPark;
import com.horeca.site.models.hotel.services.hairdresser.HairDresser;
import com.horeca.site.models.hotel.services.housekeeping.Housekeeping;
import com.horeca.site.models.hotel.services.petcare.PetCare;
import com.horeca.site.models.hotel.services.roomservice.RoomService;
import com.horeca.site.models.hotel.services.spa.Spa;
import com.horeca.site.models.hotel.services.spacall.SpaCall;
import com.horeca.site.models.hotel.services.tableordering.TableOrdering;
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

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn
    private TableOrdering tableOrdering;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn
    private Bar bar;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn
    private SpaCall spaCall;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn
    private HairDresser hairDresser;

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

    public TableOrdering getTableOrdering() {
        return tableOrdering;
    }

    public void setTableOrdering(TableOrdering tableOrdering) {
        this.tableOrdering = tableOrdering;
    }

    public Bar getBar() {
        return bar;
    }

    public void setBar(Bar bar) {
        this.bar = bar;
    }

    public SpaCall getSpaCall() {
        return spaCall;
    }

    public void setSpaCall(SpaCall spaCall) {
        this.spaCall = spaCall;
    }

    public HairDresser getHairDresser() {
        return hairDresser;
    }

    public void setHairDresser(HairDresser hairDresser) {
        this.hairDresser = hairDresser;
    }
}
