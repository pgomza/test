package com.horeca.site.services.orders;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.models.hotel.services.AvailableServiceType;
import com.horeca.site.models.hotel.services.carpark.CarPark;
import com.horeca.site.models.orders.OrderStatus;
import com.horeca.site.models.orders.Orders;
import com.horeca.site.models.orders.carpark.CarParkOrder;
import com.horeca.site.models.orders.carpark.CarParkOrderPOST;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.repositories.orders.CarParkOrderRepository;
import com.horeca.site.services.services.CarParkService;
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

    private CarParkService carParkService;
    private OrdersService ordersService;

    @Autowired
    public CarParkOrderService(ApplicationEventPublisher eventPublisher,
                               CarParkOrderRepository repository,
                               StayService stayService,
                               CarParkService carParkService,
                               OrdersService ordersService) {
        super(eventPublisher, repository, stayService);
        this.carParkService = carParkService;
        this.ordersService = ordersService;
    }

    private void ensureCanAddOrders(String pin) {
        Long hotelId = pinToHotelId(pin);
        CarPark carPark = carParkService.get(hotelId);
        if (!carPark.getAvailable()) {
            throw new BusinessRuleViolationException("The service is unavailable");
        }
    }

    public Set<CarParkOrder> getAll(String stayPin) {
        Orders orders = ordersService.get(stayPin);
        return orders.getCarParkOrders();
    }

    public CarParkOrder add(String pin, CarParkOrderPOST entity) {
        ensureCanAddOrders(pin);

        CarParkOrder newOrder = new CarParkOrder();
        newOrder.setLicenseNumber(entity.getLicenseNumber());
        newOrder.setFromDate(formatter.parseLocalDateTime(entity.getFromDate()));
        newOrder.setToDate(formatter.parseLocalDateTime(entity.getToDate()));
        newOrder.setStatus(OrderStatus.NEW);
        CarParkOrder savedOrder = repository.save(newOrder);

        Stay stay = stayService.get(pin);
        Set<CarParkOrder> carParkOrders = stay.getOrders().getCarParkOrders();
        carParkOrders.add(savedOrder);
        stayService.update(pin, stay);

        return savedOrder;
    }

    public CarParkOrder addAndNotify(String pin, CarParkOrderPOST entity) {
        ensureCanAddOrders(pin);

        CarParkOrder added = add(pin, entity);
        notifyAboutNewOrder(pin, AvailableServiceType.CARPARK);

        return added;
    }
}
