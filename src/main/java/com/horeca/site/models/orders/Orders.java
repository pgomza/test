package com.horeca.site.models.orders;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.horeca.site.models.orders.breakfast.BreakfastOrder;
import com.horeca.site.models.orders.carpark.CarParkOrder;
import com.horeca.site.models.orders.dnd.DndOrder;
import com.horeca.site.models.orders.petcare.PetCareOrder;
import com.horeca.site.models.orders.spa.SpaOrder;
import com.horeca.site.models.orders.taxi.TaxiOrder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn
    private DndOrder dnd = new DndOrder();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "orders_id_car_park")
    private Set<CarParkOrder> carParkOrders = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "orders_id_taxi")
    private Set<TaxiOrder> taxiOrders = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "orders_id_spa")
    private Set<SpaOrder> spaOrders = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "orders_id_pet_care")
    private Set<PetCareOrder> petCareOrders = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "orders_id_breakfast")
    private Set<BreakfastOrder> breakfastOrders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DndOrder getDnd() {
        return dnd;
    }

    public void setDnd(DndOrder dnd) {
        this.dnd = dnd;
    }

    public Set<CarParkOrder> getCarParkOrders() {
        return carParkOrders;
    }

    public void setCarParkOrders(Set<CarParkOrder> carParkOrders) {
        this.carParkOrders = carParkOrders;
    }

    public Set<TaxiOrder> getTaxiOrders() {
        return taxiOrders;
    }

    public void setTaxiOrders(Set<TaxiOrder> taxiOrders) {
        this.taxiOrders = taxiOrders;
    }

    public Set<SpaOrder> getSpaOrders() {
        return spaOrders;
    }

    public void setSpaOrders(Set<SpaOrder> spaOrders) {
        this.spaOrders = spaOrders;
    }

    public Set<PetCareOrder> getPetCareOrders() {
        return petCareOrders;
    }

    public void setPetCareOrders(Set<PetCareOrder> petCareOrders) {
        this.petCareOrders = petCareOrders;
    }

    public Set<BreakfastOrder> getBreakfastOrders() {
        return breakfastOrders;
    }

    public void setBreakfastOrders(Set<BreakfastOrder> breakfastOrders) {
        this.breakfastOrders = breakfastOrders;
    }
}
