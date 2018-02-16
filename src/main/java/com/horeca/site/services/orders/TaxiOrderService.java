package com.horeca.site.services.orders;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.models.hotel.services.AvailableServiceType;
import com.horeca.site.models.hotel.services.taxi.Taxi;
import com.horeca.site.models.orders.OrderStatus;
import com.horeca.site.models.orders.Orders;
import com.horeca.site.models.orders.taxi.TaxiOrder;
import com.horeca.site.models.orders.taxi.TaxiOrderPOST;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.services.services.StayService;
import com.horeca.site.services.services.TaxiService;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class TaxiOrderService extends GenericOrderService<TaxiOrder> {

    private final DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm");

    private TaxiService taxiService;
    private OrdersService ordersService;

    @Autowired
    public TaxiOrderService(ApplicationEventPublisher eventPublisher,
                            CrudRepository<TaxiOrder, Long> repository,
                            StayService stayService,
                            TaxiService taxiService,
                            OrdersService ordersService) {
        super(eventPublisher, repository, stayService);
        this.taxiService = taxiService;
        this.ordersService = ordersService;
    }

    private void ensureCanAddOrders(String pin) {
        Long hotelId = pinToHotelId(pin);
        Taxi taxi = taxiService.get(hotelId);
        if (!taxi.getAvailable()) {
            throw new BusinessRuleViolationException("The service is unavailable");
        }
    }

    public Set<TaxiOrder> getAll(String stayPin) {
        Orders orders = ordersService.get(stayPin);
        return orders.getTaxiOrders();
    }

    public TaxiOrder add(String pin, TaxiOrderPOST entity) {
        ensureCanAddOrders(pin);

        TaxiOrder newOrder = new TaxiOrder();

        LocalDateTime reservationTime = formatter.parseLocalDateTime(entity.getTime());
        newOrder.setTime(reservationTime);
        newOrder.setDestination(entity.getDestination());
        newOrder.setNumberOfPeople(entity.getNumberOfPeople());
        newOrder.setStatus(OrderStatus.NEW);

        TaxiOrder savedOrder = repository.save(newOrder);

        Stay stay = stayService.get(pin);
        Set<TaxiOrder> taxiOrders = stay.getOrders().getTaxiOrders();
        taxiOrders.add(savedOrder);
        stayService.update(pin, stay);

        return savedOrder;
    }

    public TaxiOrder addAndNotify(String pin, TaxiOrderPOST entity) {
        ensureCanAddOrders(pin);

        TaxiOrder added = add(pin, entity);
        notifyAboutNewOrder(pin, AvailableServiceType.TAXI);

        return added;
    }
}
