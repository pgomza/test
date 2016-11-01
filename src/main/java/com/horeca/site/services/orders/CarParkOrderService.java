package com.horeca.site.services.orders;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.orders.OrderStatus;
import com.horeca.site.models.orders.OrderStatusPUT;
import com.horeca.site.models.orders.Orders;
import com.horeca.site.models.orders.carpark.CarParkOrder;
import com.horeca.site.models.orders.carpark.CarParkOrderPOST;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.repositories.orders.CarParkOrderRepository;
import com.horeca.site.services.services.StayService;
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

    public Set<CarParkOrder> getAll(String stayPin) {
        Orders orders = ordersService.get(stayPin);
        Set<CarParkOrder> carParkOrders = orders.getCarParkOrders();

        return carParkOrders;
    }

    public CarParkOrder get(String stayPin, Long id) {
        CarParkOrder found = null;
        for (CarParkOrder carParkOrder : getAll(stayPin)) {
            if (carParkOrder.getId().equals(id)) {
                found = carParkOrder;
                break;
            }
        }
        if (found == null)
            throw new ResourceNotFoundException();

        return found;
    }

    public CarParkOrder add(String stayPin, CarParkOrderPOST entity) {
        CarParkOrder newOrder = new CarParkOrder();
        newOrder.setLicenseNumber(entity.getLicenseNumber());
        newOrder.setStatus(OrderStatus.NEW);
        CarParkOrder savedOrder = repository.save(newOrder);

        Stay stay = stayService.get(stayPin);
        Set<CarParkOrder> carParkOrders = stay.getOrders().getCarParkOrders();
        carParkOrders.add(savedOrder);
        stayService.update(stayPin, stay);

        return savedOrder;
    }

    public CarParkOrder update(String stayPin, Long id, CarParkOrder updated) {
        CarParkOrder order = get(stayPin, id);
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
        CarParkOrder order = get(stayPin, id);
        order.setStatus(newStatus.getStatus());
        update(stayPin, order.getId(), order);
        return newStatus;
    }
}
