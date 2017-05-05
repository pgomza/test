package com.horeca.site.services.orders;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.Price;
import com.horeca.site.models.hotel.services.AvailableServiceType;
import com.horeca.site.models.hotel.services.roomservice.RoomService;
import com.horeca.site.models.hotel.services.roomservice.RoomServiceCategory;
import com.horeca.site.models.hotel.services.roomservice.RoomServiceItem;
import com.horeca.site.models.notifications.NewOrderEvent;
import com.horeca.site.models.orders.OrderStatus;
import com.horeca.site.models.orders.Orders;
import com.horeca.site.models.orders.roomservice.RoomServiceOrder;
import com.horeca.site.models.orders.roomservice.RoomServiceOrderItem;
import com.horeca.site.models.orders.roomservice.RoomServiceOrderItemPOST;
import com.horeca.site.models.orders.roomservice.RoomServiceOrderPOST;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.services.services.StayService;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class RoomServiceOrderService extends GenericOrderService<RoomServiceOrder> {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private StayService stayService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    private DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm");

    @Override
    public Set<RoomServiceOrder> getAll(String stayPin) {
        Orders orders = ordersService.get(stayPin);
        Set<RoomServiceOrder> roomServiceOrders = orders.getRoomServiceOrders();

        return roomServiceOrders;
    }

    public RoomServiceOrder add(String stayPin, RoomServiceOrderPOST entity) {
        RoomServiceOrder roomServiceOrder = new RoomServiceOrder();

        Set<RoomServiceOrderItem> entries = new HashSet<>();
        for (RoomServiceOrderItemPOST entryPOST : entity.getItems()) {
            RoomServiceOrderItem entry = new RoomServiceOrderItem();
            RoomServiceItem item = resolveItemIdToEntity(stayPin, entryPOST.getItemId());
            entry.setItem(item);
            entry.setCount(entryPOST.getCount());
            entries.add(entry);
        }
        roomServiceOrder.setItems(entries);
        roomServiceOrder.setTime(formatter.parseLocalTime(entity.getTime()));
        roomServiceOrder.setTotal(computeTotal(entries));
        roomServiceOrder.setStatus(OrderStatus.NEW);
        RoomServiceOrder savedOrder = repository.save(roomServiceOrder);

        Stay stay = stayService.get(stayPin);
        Set<RoomServiceOrder> roomServiceOrders = stay.getOrders().getRoomServiceOrders();
        roomServiceOrders.add(savedOrder);
        stayService.update(stayPin, stay);

        return savedOrder;
    }

    public RoomServiceOrder addAndTryToNotify(String stayPin, RoomServiceOrderPOST entity) {
        RoomServiceOrder added = add(stayPin, entity);
        Stay stay = stayService.get(stayPin);

        eventPublisher.publishEvent(new NewOrderEvent(this, AvailableServiceType.ROOMSERVICE, stay));

        return added;
    }

    private RoomServiceItem resolveItemIdToEntity(String stayPin, Long id) {
        RoomService roomService = stayService.get(stayPin).getHotel().getAvailableServices().getRoomService();
        for (RoomServiceCategory category : roomService.getCategories()) {
            for (RoomServiceItem item : category.getItems()) {
                if (item.getId().equals(id)) {
                    // check if an order for this item can be placed
                    if (item.isAvailable())
                        return item;
                    else
                        throw new BusinessRuleViolationException("Item id == " + id + " is no longer available");
                }
            }
        }
        throw new ResourceNotFoundException("Could not find an item with such an id");
    }

    private Price computeTotal(Set<RoomServiceOrderItem> entries) {
        Price totalPrice = new Price();
        BigDecimal totalValue = BigDecimal.ZERO;

        for (RoomServiceOrderItem entry : entries) {
            RoomServiceItem item = entry.getItem();

            if (totalPrice.getCurrency() == null)
                totalPrice.setCurrency(item.getPrice().getCurrency());

            BigDecimal count = BigDecimal.valueOf(entry.getCount());
            totalValue = totalValue.add(item.getPrice().getValue().multiply(count));
        }

        totalPrice.setValue(totalValue);
        return totalPrice;
    }
}
