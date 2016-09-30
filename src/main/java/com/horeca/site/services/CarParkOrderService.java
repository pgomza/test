package com.horeca.site.services;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.hotel.services.carpark.CarPark;
import com.horeca.site.models.orders.OrderStatus;
import com.horeca.site.models.orders.Orders;
import com.horeca.site.models.orders.carpark.CarParkOrder;
import com.horeca.site.models.orders.carpark.CarParkOrderPOST;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.repositories.CarParkOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class CarParkOrderService {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private StayService stayService;

    @Autowired
    private CarParkOrderRepository repository;

    public Set<CarParkOrder> get(String stayPin) {
        Orders orders = ordersService.getOrders(stayPin);
        Set<CarParkOrder> carParkOrders = orders.getCarParkOrders();

        return carParkOrders;
    }

    public CarParkOrder getActive(String stayPin) {
        Set<CarParkOrder> carParkOrders = get(stayPin);

        CarParkOrder activeOrder = null;
        for (CarParkOrder order : carParkOrders) {
            if (order.getStatus() == OrderStatus.NEW || order.getStatus() == OrderStatus.ACCEPTED) {
                if (activeOrder != null) {
                    //it should never happen - the orders should be tested against the business rules while being added
                    throw new BusinessRuleViolationException("Only one car park order can be active (NEW or ACCEPTED)");
                }

                activeOrder = order;
            }
        }
        if (activeOrder == null)
            throw new ResourceNotFoundException();

        return activeOrder;
    }

    public CarParkOrder add(String stayPin, CarParkOrderPOST entity) {
        CarParkOrder newOrder = new CarParkOrder();
        newOrder.setLicensePlate(entity.getLicensePlate());
        newOrder.setStatus(OrderStatus.NEW);
        CarParkOrder savedOrder = repository.save(newOrder);

        Stay stay = stayService.get(stayPin);
        Set<CarParkOrder> carParkOrders = stay.getOrders().getCarParkOrders();
        carParkOrders.add(savedOrder);
        stayService.update(stayPin, stay);

        return savedOrder;
    }

    public CarParkOrder updateActive(String stayPin, CarParkOrder updated) {
        CarParkOrder active = getActive(stayPin);
        updated.setId(active.getId());
        return repository.save(updated);
    }
}
