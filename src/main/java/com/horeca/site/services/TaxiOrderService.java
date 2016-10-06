package com.horeca.site.services;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.orders.OrderStatus;
import com.horeca.site.models.orders.OrderStatusPUT;
import com.horeca.site.models.orders.Orders;
import com.horeca.site.models.orders.taxi.TaxiOrder;
import com.horeca.site.models.orders.taxi.TaxiOrderPOST;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.repositories.TaxiOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class TaxiOrderService {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private StayService stayService;

    @Autowired
    private TaxiOrderRepository repository;

    public Set<TaxiOrder> getAll(String stayPin) {
        Orders orders = ordersService.get(stayPin);
        Set<TaxiOrder> taxiOrders = orders.getTaxiOrders();

        return taxiOrders;
    }

    public TaxiOrder get(String stayPin, Long id) {
        TaxiOrder found = null;
        for (TaxiOrder taxiOrder : getAll(stayPin)) {
            if (taxiOrder.getId().equals(id)) {
                found = taxiOrder;
                break;
            }
        }
        if (found == null)
            throw new ResourceNotFoundException();

        return found;
    }

    public TaxiOrder add(String stayPin, TaxiOrderPOST entity) {
        TaxiOrder newOrder = new TaxiOrder();
        newOrder.setTime(entity.getTime());
        newOrder.setStatus(OrderStatus.NEW);
        TaxiOrder savedOrder = repository.save(newOrder);

        Stay stay = stayService.get(stayPin);
        Set<TaxiOrder> taxiOrders = stay.getOrders().getTaxiOrders();
        taxiOrders.add(savedOrder);
        stayService.update(stayPin, stay);

        return savedOrder;
    }

    public TaxiOrder update(String stayPin, Long id, TaxiOrder updated) {
        TaxiOrder order = get(stayPin, id);
        updated.setId(order.getId());
        return repository.save(updated);
    }

    public OrderStatusPUT getStatus(String pin, Long id) {
        OrderStatus status = get(pin, id).getStatus();
        OrderStatusPUT statusPUT = new OrderStatusPUT();
        statusPUT.setStatus(status);
        return statusPUT;
    }

    public OrderStatusPUT updateStatus(String stayPin, Long id, OrderStatusPUT newStatus) {
        TaxiOrder order = get(stayPin, id);
        order.setStatus(newStatus.getStatus());
        update(stayPin, order.getId(), order);
        return newStatus;
    }
}
