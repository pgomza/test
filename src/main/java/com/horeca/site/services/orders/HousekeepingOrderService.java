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
import com.horeca.site.services.services.StayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class HousekeepingOrderService extends GenericOrderService<HousekeepingOrder> {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private StayService stayService;

    @Autowired
    private HousekeepingOrderRepository repository;

    @Override
    protected CrudRepository<HousekeepingOrder, Long> getRepository() {
        return repository;
    }

    public Set<HousekeepingOrder> getAll(String stayPin) {
        Orders orders = ordersService.get(stayPin);
        return orders.getHousekeepingOrders();
    }

    public HousekeepingOrder add(String stayPin, HousekeepingOrderPOST orderPOST) {
        Set<HousekeepingItemData> dataItems = new HashSet<>();
        for (Long itemId : orderPOST.getItemIds()) {
            HousekeepingItem housekeepingItem = resolveItemIdToEntity(stayPin, itemId);
            HousekeepingItemData itemData = new HousekeepingItemData(housekeepingItem.getName());
            dataItems.add(itemData);
        }

        HousekeepingOrder order = new HousekeepingOrder(orderPOST.getMessage(), dataItems);
        HousekeepingOrder savedOrder = repository.save(order);

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
                    return item;
            }
        }
        throw new ResourceNotFoundException("Could not find an item with such an id");
    }
}
