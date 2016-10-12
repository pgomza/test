package com.horeca.site.services;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.hotel.services.spa.Spa;
import com.horeca.site.models.hotel.services.spa.SpaItem;
import com.horeca.site.models.orders.OrderStatus;
import com.horeca.site.models.orders.OrderStatusPUT;
import com.horeca.site.models.orders.Orders;
import com.horeca.site.models.orders.spa.SpaOrder;
import com.horeca.site.models.orders.spa.SpaOrderPOST;
import com.horeca.site.models.orders.spa.SpaOrderView;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.repositories.SpaOrderRepository;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private SpaOrderRepository repository;

    private DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm");

    public Set<SpaOrder> getAll(String stayPin) {
        Orders orders = ordersService.get(stayPin);
        Set<SpaOrder> spaOrders = orders.getSpaOrders();

        return spaOrders;
    }

    public Set<SpaOrderView> getAllViews(String stayPin, String preferredLanguage) {
        String defaultLanguage = stayService.get(stayPin).getHotel().getDefaultTranslation();
        Set<SpaOrderView> views = new HashSet<>();
        for (SpaOrder spaOrder : getAll(stayPin)) {
            views.add(spaOrder.toView(preferredLanguage, defaultLanguage));
        }
        return views;
    }

    public SpaOrder get(String stayPin, Long id) {
        SpaOrder found = null;
        for (SpaOrder spaOrder : getAll(stayPin)) {
            if (spaOrder.getId().equals(id)) {
                found = spaOrder;
                break;
            }
        }
        if (found == null)
            throw new ResourceNotFoundException();

        return found;
    }

    public SpaOrderView getView(String stayPin, Long id, String preferredLanguage) {
        String defaultLanguage = stayService.get(stayPin).getHotel().getDefaultTranslation();
        return get(stayPin, id).toView(preferredLanguage, defaultLanguage);
    }

    public SpaOrder add(String stayPin, SpaOrderPOST entity) {
        SpaOrder newOrder = new SpaOrder();
        newOrder.setItem(resolveItemIdToEntity(stayPin, entity.getItemId()));
        newOrder.setStatus(OrderStatus.NEW);
        newOrder.setTime(formatter.parseDateTime(entity.getTime()));
        SpaOrder savedOrder = repository.save(newOrder);

        Stay stay = stayService.get(stayPin);
        Set<SpaOrder> spaOrders = stay.getOrders().getSpaOrders();
        spaOrders.add(savedOrder);
        stayService.update(stayPin, stay);

        return savedOrder;
    }

    public SpaOrder update(String stayPin, Long id, SpaOrder updated) {
        SpaOrder order = get(stayPin, id);
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
        SpaOrder order = get(stayPin, id);
        order.setStatus(newStatus.getStatus());
        update(stayPin, order.getId(), order);
        return newStatus;
    }

    private SpaItem resolveItemIdToEntity(String stayPin, Long id) {
        Spa spa = stayService.get(stayPin).getHotel().getAvailableServices().getSpa();
        for (SpaItem item : spa.getItems()) {
            if (item.getId().equals(id))
                return item;
        }

        throw new ResourceNotFoundException();
    }
}
