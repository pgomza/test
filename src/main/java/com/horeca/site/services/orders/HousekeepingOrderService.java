package com.horeca.site.services.orders;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.hotel.services.AvailableServiceType;
import com.horeca.site.models.hotel.services.housekeeping.Housekeeping;
import com.horeca.site.models.hotel.services.housekeeping.HousekeepingItem;
import com.horeca.site.models.orders.Orders;
import com.horeca.site.models.orders.housekeeping.HousekeepingItemData;
import com.horeca.site.models.orders.housekeeping.HousekeepingOrder;
import com.horeca.site.models.orders.housekeeping.HousekeepingOrderPOST;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.repositories.orders.HousekeepingOrderRepository;
import com.horeca.site.services.services.HousekeepingService;
import com.horeca.site.services.services.StayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class HousekeepingOrderService extends GenericOrderService<HousekeepingOrder> {

    private HousekeepingService housekeepingService;
    private OrdersService ordersService;

    @Autowired
    public HousekeepingOrderService(ApplicationEventPublisher eventPublisher,
                                    HousekeepingOrderRepository repository,
                                    StayService stayService,
                                    HousekeepingService housekeepingService,
                                    OrdersService ordersService) {
        super(eventPublisher, repository, stayService);
        this.housekeepingService = housekeepingService;
        this.ordersService = ordersService;
    }

    private void ensureCanAddOrders(String pin) {
        Long hotelId = pinToHotelId(pin);
        Housekeeping housekeeping = housekeepingService.get(hotelId);
        if (!housekeeping.getAvailable()) {
            throw new AccessDeniedException("The service is unavailable");
        }
    }

    public Set<HousekeepingOrder> getAll(String stayPin) {
        Orders orders = ordersService.get(stayPin);
        return orders.getHousekeepingOrders();
    }

    public HousekeepingOrder add(String pin, HousekeepingOrderPOST orderPOST) {
        ensureCanAddOrders(pin);

        Set<HousekeepingItemData> dataItems = new HashSet<>();
        for (Long itemId : orderPOST.getItemIds()) {
            HousekeepingItem housekeepingItem = resolveItemIdToEntity(pin, itemId);
            HousekeepingItemData itemData = new HousekeepingItemData(housekeepingItem.getName());
            dataItems.add(itemData);
        }

        HousekeepingOrder order = new HousekeepingOrder(orderPOST.getMessage(), dataItems);
        HousekeepingOrder savedOrder = repository.save(order);

        Stay stay = stayService.get(pin);
        Set<HousekeepingOrder> housekeepingOrders = stay.getOrders().getHousekeepingOrders();
        housekeepingOrders.add(savedOrder);
        stayService.update(pin, stay);

        return savedOrder;
    }

    public HousekeepingOrder addAndNotify(String pin, HousekeepingOrderPOST entity) {
        ensureCanAddOrders(pin);

        HousekeepingOrder added = add(pin, entity);
        notifyAboutNewOrder(pin, AvailableServiceType.HOUSEKEEPING);

        return added;
    }

    private HousekeepingItem resolveItemIdToEntity(String stayPin, Long id) {
        Housekeeping housekeeping = stayService.get(stayPin).getHotel().getAvailableServices().getHousekeeping();

        for (HousekeepingItem item : housekeeping.getItems()) {
            if (item.getId().equals(id)) {
                    return item;
            }
        }
        throw new ResourceNotFoundException("Could not find an item with such an id");
    }
}
