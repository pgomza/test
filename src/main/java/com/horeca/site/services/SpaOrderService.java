package com.horeca.site.services;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.Currency;
import com.horeca.site.models.Price;
import com.horeca.site.models.hotel.services.spa.Spa;
import com.horeca.site.models.hotel.services.spa.SpaItem;
import com.horeca.site.models.orders.OrderStatus;
import com.horeca.site.models.orders.OrderStatusPUT;
import com.horeca.site.models.orders.Orders;
import com.horeca.site.models.orders.carpark.CarParkOrder;
import com.horeca.site.models.orders.spa.*;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.repositories.SpaOrderRepository;
import com.horeca.site.repositories.StayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class SpaOrderService {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private StayService stayService;

    @Autowired
    private StayRepository stayRepository;

    @Autowired
    private SpaOrderRepository repository;

    public Set<SpaOrder> get(String stayPin) {
        Orders orders = ordersService.getOrders(stayPin);
        return orders.getSpaOrders();
    }

    public SpaOrder getActive(String stayPin) {
        Set<SpaOrder> spaOrders = get(stayPin);

        SpaOrder activeOrder = null;
        for (SpaOrder order : spaOrders) {
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

    public SpaOrderView getActiveView(String stayPin, String preferredLanguage) {
        SpaOrder spaOrder = getActive(stayPin);
        String defaultLanguage = stayService.get(stayPin).getHotel().getDefaultTranslation();
        return spaOrder.toView(preferredLanguage, defaultLanguage);
    }

    public SpaOrder add(String stayPin, SpaOrderPOST entity) {
        SpaOrder newOrder = new SpaOrder();
        newOrder.setStatus(OrderStatus.NEW);

        Set<SpaOrderEntry> entries = new HashSet<>();
        for (SpaOrderEntryPOST entryPOST : entity.getItems()) {
            SpaOrderEntry entry = new SpaOrderEntry();

            SpaItem item = resolveItemIdToEntity(stayPin, entryPOST.getId());
            entry.setItem(item);
            entry.setDay(entryPOST.getDay());
            entry.setHour(entryPOST.getHour());

            entries.add(entry);
        }
        newOrder.setItems(entries);
        newOrder.setTotal(computeTotal(stayPin, newOrder.getItems()));
        SpaOrder savedOrder = repository.save(newOrder);

        Stay stay = stayService.get(stayPin);
        Set<SpaOrder> spaOrders = stay.getOrders().getSpaOrders();
        spaOrders.add(savedOrder);
        stayService.update(stayPin, stay);

        return savedOrder;
    }

    private SpaItem resolveItemIdToEntity(String stayPin, Long id) {
        Spa spa = stayService.get(stayPin).getHotel().getAvailableServices().getSpa();
        for (SpaItem item : spa.getItems()) {
            if (item.getId().equals(id))
                return item;
        }
        throw new ResourceNotFoundException();
    }

    private Price computeTotal(String stayPin, Set<SpaOrderEntry> entries) {
        Price totalPrice = new Price();
        totalPrice.setValue(BigDecimal.ZERO);
        totalPrice.setCurrency(Currency.DOLLAR);

        return totalPrice; //return 0.0 for now
    }

    public SpaOrder updateActive(String stayPin, SpaOrder updated) {
        SpaOrder active = getActive(stayPin);
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
        SpaOrder active = getActive(stayPin);
        active.setStatus(newStatus.getStatus());
        updateActive(stayPin, active);
        return newStatus;
    }
}
