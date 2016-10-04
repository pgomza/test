package com.horeca.site.models.orders;

import com.horeca.site.models.orders.carpark.CarParkOrder;
import com.horeca.site.models.orders.dnd.DndOrder;
import com.horeca.site.models.orders.taxi.TaxiOrder;

import java.util.Set;

public class OrdersView {

    private DndOrder dnd;

    private Set<CarParkOrder> carParkOrders;

    private Set<TaxiOrder> taxiOrders;

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
}
