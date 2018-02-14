package com.horeca.site.services.orders;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.hotel.services.AvailableServiceType;
import com.horeca.site.models.hotel.services.housekeeping.Housekeeping;
import com.horeca.site.models.hotel.services.housekeeping.HousekeepingItem;
import com.horeca.site.models.orders.OrderStatus;
import com.horeca.site.models.orders.Orders;
import com.horeca.site.models.orders.housekeeping.HousekeepingOrder;
import com.horeca.site.models.orders.housekeeping.HousekeepingOrderPOST;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.repositories.orders.HousekeepingOrderRepository;
import com.horeca.site.services.services.StayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class HousekeepingOrderService extends GenericOrderService<HousekeepingOrder> {

    private OrdersService ordersService;

    @Autowired
    public HousekeepingOrderService(ApplicationEventPublisher eventPublisher,
                                    HousekeepingOrderRepository repository,
                                    StayService stayService,
                                    OrdersService ordersService) {
        super(eventPublisher, repository, stayService);
        this.ordersService = ordersService;
    }

    public Set<HousekeepingOrder> getAll(String stayPin) {
        Orders orders = ordersService.get(stayPin);
        return orders.getHousekeepingOrders();
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

    public HousekeepingOrder addAndNotify(String stayPin, HousekeepingOrderPOST entity) {
        HousekeepingOrder added = add(stayPin, entity);
        notifyAboutNewOrder(stayPin, AvailableServiceType.HOUSEKEEPING);

        return added;
    }

    private HousekeepingItem resolveItemIdToEntity(String stayPin, Long id) {
        Housekeeping housekeeping = stayService.get(stayPin).getHotel().getAvailableServices().getHousekeeping();

        for (HousekeepingItem item : housekeeping.getItems()) {
            if (item.getId().equals(id)) {
                if (item.isAvailable())
                    return item;
                else
                    throw new BusinessRuleViolationException("Item id == " + id + " is no longer available");
            }
        }
        throw new ResourceNotFoundException("Could not find an item with such an id");
    }
}
