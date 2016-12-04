package com.horeca.site.models.orders;

import com.horeca.site.models.orders.breakfast.BreakfastOrderView;
import com.horeca.site.models.orders.carpark.CarParkOrder;
import com.horeca.site.models.orders.dnd.DndOrder;
import com.horeca.site.models.orders.petcare.PetCareOrderView;
import com.horeca.site.models.orders.spa.SpaOrderView;
import com.horeca.site.models.orders.taxi.TaxiOrder;

import java.util.Set;

public class OrdersView {

    private DndOrder dndOrder;

    private Set<CarParkOrder> carParkOrders;

    private Set<TaxiOrder> taxiOrders;

    private Set<SpaOrderView> spaOrders;

    private Set<PetCareOrderView> petCareOrders;

    private Set<BreakfastOrderView> breakfastOrders;

    public DndOrder getDndOrder() {
        return dndOrder;
    }

    public void setDndOrder(DndOrder dndOrder) {
        this.dndOrder = dndOrder;
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

    public Set<SpaOrderView> getSpaOrders() {
        return spaOrders;
    }

    public void setSpaOrders(Set<SpaOrderView> spaOrders) {
        this.spaOrders = spaOrders;
    }

    public Set<PetCareOrderView> getPetCareOrders() {
        return petCareOrders;
    }

    public void setPetCareOrders(Set<PetCareOrderView> petCareOrders) {
        this.petCareOrders = petCareOrders;
    }

    public Set<BreakfastOrderView> getBreakfastOrders() {
        return breakfastOrders;
    }

    public void setBreakfastOrders(Set<BreakfastOrderView> breakfastOrders) {
        this.breakfastOrders = breakfastOrders;
    }
}
