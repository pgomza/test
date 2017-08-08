package com.horeca.site.services.orders;

import com.horeca.site.models.hotel.services.AvailableServiceType;
import com.horeca.site.models.orders.OrderStatus;
import com.horeca.site.models.orders.Orders;
import com.horeca.site.models.orders.taxi.TaxiOrder;
import com.horeca.site.models.orders.taxi.TaxiOrderPOST;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.repositories.orders.TaxiOrderRepository;
import com.horeca.site.services.services.StayService;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class TaxiOrderService extends GenericOrderService<TaxiOrder> {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private StayService stayService;

    @Autowired
    private TaxiOrderRepository repository;

    private DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm");

    @Override
    protected CrudRepository<TaxiOrder, Long> getRepository() {
        return repository;
    }

    public Set<TaxiOrder> getAll(String stayPin) {
        Orders orders = ordersService.get(stayPin);
        return orders.getTaxiOrders();
    }

    public TaxiOrder add(String stayPin, TaxiOrderPOST entity) {
        TaxiOrder newOrder = new TaxiOrder();

        LocalDateTime reservationTime = formatter.parseLocalDateTime(entity.getTime());
        newOrder.setTime(reservationTime);
        newOrder.setDestination(entity.getDestination());
        newOrder.setNumberOfPeople(entity.getNumberOfPeople());
        newOrder.setStatus(OrderStatus.NEW);

        TaxiOrder savedOrder = repository.save(newOrder);

        Stay stay = stayService.get(stayPin);
        Set<TaxiOrder> taxiOrders = stay.getOrders().getTaxiOrders();
        taxiOrders.add(savedOrder);
        stayService.update(stayPin, stay);

        return savedOrder;
    }

    public TaxiOrder addAndNotify(String stayPin, TaxiOrderPOST entity) {
        TaxiOrder added = add(stayPin, entity);
        notifyAboutNewOrder(stayPin, AvailableServiceType.TAXI);

        return added;
    }
}
