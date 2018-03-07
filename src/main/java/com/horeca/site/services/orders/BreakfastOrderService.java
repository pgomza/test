package com.horeca.site.services.orders;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.Price;
import com.horeca.site.models.hotel.services.AvailableServiceType;
import com.horeca.site.models.hotel.services.breakfast.Breakfast;
import com.horeca.site.models.hotel.services.breakfast.BreakfastCategory;
import com.horeca.site.models.hotel.services.breakfast.BreakfastItem;
import com.horeca.site.models.orders.Orders;
import com.horeca.site.models.orders.ServiceItemDataWithPrice;
import com.horeca.site.models.orders.breakfast.BreakfastOrder;
import com.horeca.site.models.orders.breakfast.BreakfastOrderItem;
import com.horeca.site.models.orders.breakfast.BreakfastOrderItemPOST;
import com.horeca.site.models.orders.breakfast.BreakfastOrderPOST;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.repositories.orders.BreakfastOrderRepository;
import com.horeca.site.services.services.StayService;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class BreakfastOrderService extends GenericOrderService<BreakfastOrder> {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private StayService stayService;

    @Autowired
    private BreakfastOrderRepository repository;

    private DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm");

    @Override
    protected CrudRepository<BreakfastOrder, Long> getRepository() {
        return repository;
    }

    public Set<BreakfastOrder> getAll(String stayPin) {
        Orders orders = ordersService.get(stayPin);
        return orders.getBreakfastOrders();
    }

    public BreakfastOrder add(String stayPin, BreakfastOrderPOST orderPOST) {
        Set<BreakfastOrderItem> orderItemSet = new HashSet<>();
        for (BreakfastOrderItemPOST entryPOST : orderPOST.getItems()) {
            BreakfastItem serviceItem = resolveItemIdToEntity(stayPin, entryPOST.getItemId());
            // copy the data from item
            ServiceItemDataWithPrice itemData = new ServiceItemDataWithPrice(serviceItem.getName(), serviceItem.getPrice());
            BreakfastOrderItem orderItem = new BreakfastOrderItem(itemData, entryPOST.getCount());
            orderItemSet.add(orderItem);
        }
        LocalDateTime orderTime = formatter.parseLocalDateTime(orderPOST.getTime());
        Price totalPrice = computeTotal(orderItemSet);
        BreakfastOrder order = new BreakfastOrder(totalPrice, orderTime, orderItemSet);
        BreakfastOrder savedOrder = repository.save(order);

        Stay stay = stayService.get(stayPin);
        Set<BreakfastOrder> roomServiceOrders = stay.getOrders().getBreakfastOrders();
        roomServiceOrders.add(savedOrder);
        stayService.update(stayPin, stay);

        return savedOrder;
    }

    public BreakfastOrder addAndNotify(String stayPin, BreakfastOrderPOST entity) {
        BreakfastOrder added = add(stayPin, entity);
        notifyAboutNewOrder(stayPin, AvailableServiceType.BREAKFAST);

        return added;
    }

    private BreakfastItem resolveItemIdToEntity(String stayPin, Long id) {
        Breakfast breakfast = stayService.get(stayPin).getHotel().getAvailableServices().getBreakfast();
        for (BreakfastCategory category : breakfast.getCategories()) {
            for (BreakfastItem item : category.getItems()) {
                if (item.getId().equals(id)) {
                        return item;
                }
            }
        }
        throw new ResourceNotFoundException("Could not find an item with such an id");
    }

    private Price computeTotal(Set<BreakfastOrderItem> entries) {
        Price totalPrice = new Price();
        BigDecimal totalValue = BigDecimal.ZERO;

        for (BreakfastOrderItem entry : entries) {
            ServiceItemDataWithPrice item = entry.getItem();

            if (totalPrice.getCurrency() == null)
                totalPrice.setCurrency(item.getPrice().getCurrency());

            BigDecimal count = BigDecimal.valueOf(entry.getCount());
            totalValue = totalValue.add(item.getPrice().getValue().multiply(count));
        }

        totalPrice.setValue(totalValue);
        return totalPrice;
    }
}
