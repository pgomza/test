package com.horeca.site.models.orders;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.horeca.site.models.Viewable;
import com.horeca.site.models.hotel.services.petcare.PetCare;
import com.horeca.site.models.orders.breakfast.BreakfastOrder;
import com.horeca.site.models.orders.breakfast.BreakfastOrderView;
import com.horeca.site.models.orders.carpark.CarParkOrder;
import com.horeca.site.models.orders.dnd.DndOrder;
import com.horeca.site.models.orders.petcare.PetCareOrder;
import com.horeca.site.models.orders.petcare.PetCareOrderView;
import com.horeca.site.models.orders.spa.SpaOrder;
import com.horeca.site.models.orders.spa.SpaOrderView;
import com.horeca.site.models.orders.taxi.TaxiOrder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Orders implements Viewable<OrdersView> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn
    private DndOrder dnd = new DndOrder();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn
    private Set<CarParkOrder> carParkOrders = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn
    private Set<TaxiOrder> taxiOrders = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn
    private Set<SpaOrder> spaOrders = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn
    private Set<PetCareOrder> petCareOrders = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn
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

    @Override
    public OrdersView toView(String preferredLanguage, String defaultLanguage) {
        OrdersView view = new OrdersView();
        view.setDndOrder(getDnd());
        view.setCarParkOrders(getCarParkOrders());
        view.setTaxiOrders(getTaxiOrders());

        Set<SpaOrderView> spaOrderViews = new HashSet<>();
        for (SpaOrder spaOrder : getSpaOrders()) {
            spaOrderViews.add(spaOrder.toView(preferredLanguage, defaultLanguage));
        }
        view.setSpaOrders(spaOrderViews);

        Set<PetCareOrderView> petCareOrderViews = new HashSet<>();
        for (PetCareOrder petCareOrder : getPetCareOrders()) {
            petCareOrderViews.add(petCareOrder.toView(preferredLanguage, defaultLanguage));
        }
        view.setPetCareOrders(petCareOrderViews);

        Set<BreakfastOrderView> breakfastOrderViews = new HashSet<>();
        for (BreakfastOrder breakfastOrder : getBreakfastOrders()) {
            breakfastOrderViews.add(breakfastOrder.toView(preferredLanguage, defaultLanguage));
        }
        view.setBreakfastOrders(breakfastOrderViews);

        return view;
    }
}
