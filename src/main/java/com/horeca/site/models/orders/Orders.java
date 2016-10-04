package com.horeca.site.models.orders;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.horeca.site.models.Viewable;
import com.horeca.site.models.orders.carpark.CarParkOrder;
import com.horeca.site.models.orders.dnd.DndOrder;
import com.horeca.site.models.orders.taxi.TaxiOrder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Orders implements Viewable<OrdersView> {

    @Id
    @GeneratedValue
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

    @Override
    public OrdersView toView(String preferredLanguage, String defaultLanguage) {
        OrdersView view = new OrdersView();
        view.setDnd(getDnd());
        view.setCarParkOrders(getCarParkOrders());
        view.setTaxiOrders(getTaxiOrders());
        return view;
    }
}
