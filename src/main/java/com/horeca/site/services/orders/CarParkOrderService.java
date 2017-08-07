package com.horeca.site.services.orders;

import com.horeca.site.models.hotel.services.AvailableServiceType;
import com.horeca.site.models.orders.OrderStatus;
import com.horeca.site.models.orders.Orders;
import com.horeca.site.models.orders.carpark.CarParkOrder;
import com.horeca.site.models.orders.carpark.CarParkOrderPOST;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.repositories.orders.CarParkOrderRepository;
import com.horeca.site.services.services.StayService;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class CarParkOrderService extends GenericOrderService<CarParkOrder> {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private StayService stayService;

    @Autowired
    private CarParkOrderRepository repository;

    private DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm");

    @Override
    protected CrudRepository<CarParkOrder, Long> getRepository() {
        return repository;
    }

    public Set<CarParkOrder> getAll(String stayPin) {
        notifyAboutNewOrder(stayPin, AvailableServiceType.BAR);
        Orders orders = ordersService.get(stayPin);
        return orders.getCarParkOrders();
    }

    public CarParkOrder add(String stayPin, CarParkOrderPOST entity) {
        CarParkOrder newOrder = new CarParkOrder();
        newOrder.setLicenseNumber(entity.getLicenseNumber());
        newOrder.setFromDate(formatter.parseLocalDateTime(entity.getFromDate()));
        newOrder.setToDate(formatter.parseLocalDateTime(entity.getToDate()));
        newOrder.setStatus(OrderStatus.NEW);
        CarParkOrder savedOrder = repository.save(newOrder);

        Stay stay = stayService.get(stayPin);
        Set<CarParkOrder> carParkOrders = stay.getOrders().getCarParkOrders();
        carParkOrders.add(savedOrder);
        stayService.update(stayPin, stay);

        return savedOrder;
    }

    public CarParkOrder addAndNotify(String stayPin, CarParkOrderPOST entity) {
        CarParkOrder added = add(stayPin, entity);
        notifyAboutNewOrder(stayPin, AvailableServiceType.CARPARK);

        return added;
    }
}
