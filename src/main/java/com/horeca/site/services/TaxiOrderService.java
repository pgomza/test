package com.horeca.site.services;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.orders.OrderStatus;
import com.horeca.site.models.orders.OrderStatusPUT;
import com.horeca.site.models.orders.Orders;
import com.horeca.site.models.orders.carpark.CarParkOrder;
import com.horeca.site.models.orders.carpark.CarParkOrderPOST;
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

    public Set<TaxiOrder> get(String stayPin) {
        Orders orders = ordersService.getOrders(stayPin);
        Set<TaxiOrder> taxiOrders = orders.getTaxiOrders();

        return taxiOrders;
    }

    public TaxiOrder getActive(String stayPin) {
        Set<TaxiOrder> taxiOrders = get(stayPin);

        TaxiOrder activeOrder = null;
        for (TaxiOrder order : taxiOrders) {
            if (order.getStatus() == OrderStatus.NEW || order.getStatus() == OrderStatus.ACCEPTED) {
                if (activeOrder != null) {
                    //it should never happen - the orders should be tested against that business rule while being added
                    throw new BusinessRuleViolationException("Only one car park order can be active (NEW or ACCEPTED)");
                }

                activeOrder = order;
            }
        }
        if (activeOrder == null)
            throw new ResourceNotFoundException();

        return activeOrder;
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

    public TaxiOrder updateActive(String stayPin, TaxiOrder updated) {
        TaxiOrder active = getActive(stayPin);
        updated.setId(active.getId());
        return repository.save(updated);
    }

    public OrderStatusPUT getActiveStatus(String pin) {
        OrderStatus status = getActive(pin).getStatus();
        OrderStatusPUT statusPUT = new OrderStatusPUT();
        statusPUT.setStatus(status);
        return statusPUT;
    }

    public OrderStatusPUT updateActiveStatus(String stayPin, OrderStatusPUT newStatus) {
        TaxiOrder active = getActive(stayPin);
        active.setStatus(newStatus.getStatus());
        updateActive(stayPin, active);
        return newStatus;
    }
}
