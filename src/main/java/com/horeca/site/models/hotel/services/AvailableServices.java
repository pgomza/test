package com.horeca.site.models.hotel.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.horeca.site.models.Translatable;
import com.horeca.site.models.Viewable;
import com.horeca.site.models.hotel.services.breakfast.Breakfast;
import com.horeca.site.models.hotel.services.breakfast.BreakfastView;
import com.horeca.site.models.hotel.services.carpark.CarPark;
import com.horeca.site.models.hotel.services.carpark.CarParkView;
import com.horeca.site.models.hotel.services.petcare.PetCare;
import com.horeca.site.models.hotel.services.petcare.PetCareView;
import com.horeca.site.models.hotel.services.receptioncall.ReceptionCall;
import com.horeca.site.models.hotel.services.roomservice.RoomService;
import com.horeca.site.models.hotel.services.roomservice.RoomServiceView;
import com.horeca.site.models.hotel.services.spa.Spa;
import com.horeca.site.models.hotel.services.spa.SpaView;
import com.horeca.site.models.hotel.services.taxi.Taxi;

import javax.persistence.*;

@Entity
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class AvailableServices implements Viewable<AvailableServicesView> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn
    private Breakfast breakfast;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn
    private CarPark carPark;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn
    private RoomService roomService;

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

    public RoomService getRoomService() {
        return roomService;
    }

    public void setRoomService(RoomService roomService) {
        this.roomService = roomService;
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

    @Override
    public AvailableServicesView toView(String preferredLanguage, String defaultLanguage) {
        AvailableServicesView view = new AvailableServicesView();
        view.setId(getId());

        BreakfastView breakfastView = getBreakfast().toView(preferredLanguage, defaultLanguage);
        view.setBreakfast(breakfastView);

        CarParkView carParkView = getCarPark().toView(preferredLanguage, defaultLanguage);
        view.setCarPark(carParkView);

        RoomServiceView roomServiceView = getRoomService().toView(preferredLanguage, defaultLanguage);
        view.setRoomService(roomServiceView);

        SpaView spaView = getSpa().toView(preferredLanguage, defaultLanguage);
        view.setSpa(spaView);

        PetCareView petCareView = getPetCare().toView(preferredLanguage, defaultLanguage);
        view.setPetCare(petCareView);

        view.setTaxi(getTaxi());

        view.setReceptionCall(getReceptionCall());

        return view;
    }
}
