package com.horeca.site.services.orders;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.hotel.services.housekeeping.Housekeeping;
import com.horeca.site.models.hotel.services.housekeeping.HousekeepingItem;
import com.horeca.site.models.orders.OrderStatus;
import com.horeca.site.models.orders.OrderStatusPUT;
import com.horeca.site.models.orders.Orders;
import com.horeca.site.models.orders.housekeeping.HousekeepingOrder;
import com.horeca.site.models.orders.housekeeping.HousekeepingOrderPOST;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.repositories.orders.HousekeepingOrderRepository;
import com.horeca.site.services.services.StayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class HousekeepingOrderService {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private StayService stayService;

    @Autowired
    private HousekeepingOrderRepository repository;

    public Set<HousekeepingOrder> getAll(String stayPin) {
        Orders orders = ordersService.get(stayPin);
        Set<HousekeepingOrder> housekeepingOrders = orders.getHousekeepingOrders();

        return housekeepingOrders;
    }

    public HousekeepingOrder get(String stayPin, Long id) {
        HousekeepingOrder order = null;
        for (HousekeepingOrder housekeepingOrder : getAll(stayPin)) {
            if (housekeepingOrder.getId().equals(id)) {
                order = housekeepingOrder;
                break;
            }
        }
        if (order == null)
            throw new ResourceNotFoundException("Could not find an order with such an id");

        return order;
    }

    public HousekeepingOrder add(String stayPin, HousekeepingOrderPOST entity) {
        HousekeepingOrder housekeepingOrder = new HousekeepingOrder();

        Set<HousekeepingItem> orderedItems = new HashSet<>();
        for (Long itemId : entity.getItemIds()) {
            HousekeepingItem housekeepingItem = resolveItemIdToEntity(stayPin, itemId);
            orderedItems.add(housekeepingItem);
        }

        housekeepingOrder.setItems(orderedItems);
        housekeepingOrder.setMessage(entity.getMessage());
        housekeepingOrder.setStatus(OrderStatus.NEW);
        HousekeepingOrder savedOrder = repository.save(housekeepingOrder);

        Stay stay = stayService.get(stayPin);
        Set<HousekeepingOrder> housekeepingOrders = stay.getOrders().getHousekeepingOrders();
        housekeepingOrders.add(savedOrder);
        stayService.update(stayPin, stay);

        return savedOrder;
    }

    public HousekeepingOrder update(String stayPin, Long id, HousekeepingOrder updated) {
        HousekeepingOrder order = get(stayPin, id);
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
        HousekeepingOrder order = get(stayPin, id);
        order.setStatus(newStatus.getStatus());
        update(stayPin, order.getId(), order);
        return newStatus;
    }

    private HousekeepingItem resolveItemIdToEntity(String stayPin, Long id) {
        Housekeeping housekeeping = stayService.get(stayPin).getHotel().getAvailableServices().getHousekeeping();

        for (HousekeepingItem item : housekeeping.getItems()) {
            if (item.getId().equals(id))
                return item;
        }
        throw new ResourceNotFoundException("Could not find an item with such an id");
    }
}
