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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class CarParkOrderService extends GenericOrderService<CarParkOrder> {

    private final DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm");

    private OrdersService ordersService;

    @Autowired
    public CarParkOrderService(ApplicationEventPublisher eventPublisher,
                               CarParkOrderRepository repository,
                               StayService stayService,
                               OrdersService ordersService) {
        super(eventPublisher, repository, stayService);
        this.ordersService = ordersService;
    }

    public Set<CarParkOrder> getAll(String stayPin) {
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
