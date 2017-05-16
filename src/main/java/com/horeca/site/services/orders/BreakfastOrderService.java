package com.horeca.site.services.orders;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.Price;
import com.horeca.site.models.hotel.services.AvailableServiceType;
import com.horeca.site.models.hotel.services.breakfast.Breakfast;
import com.horeca.site.models.hotel.services.breakfast.BreakfastCategory;
import com.horeca.site.models.hotel.services.breakfast.BreakfastItem;
import com.horeca.site.models.notifications.NewOrderEvent;
import com.horeca.site.models.orders.OrderStatus;
import com.horeca.site.models.orders.OrderStatusPUT;
import com.horeca.site.models.orders.Orders;
import com.horeca.site.models.orders.breakfast.BreakfastOrder;
import com.horeca.site.models.orders.breakfast.BreakfastOrderItem;
import com.horeca.site.models.orders.breakfast.BreakfastOrderItemPOST;
import com.horeca.site.models.orders.breakfast.BreakfastOrderPOST;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.repositories.orders.BreakfastOrderRepository;
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
public class BreakfastOrderService {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private StayService stayService;

    @Autowired
    private BreakfastOrderRepository repository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    private DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm");

    public Set<BreakfastOrder> getAll(String stayPin) {
        Orders orders = ordersService.get(stayPin);
        Set<BreakfastOrder> breakfastOrders = orders.getBreakfastOrders();

        return breakfastOrders;
    }

    public BreakfastOrder get(String stayPin, Long id) {
        BreakfastOrder found = null;
        for (BreakfastOrder breakfastOrder : getAll(stayPin)) {
            if (breakfastOrder.getId().equals(id)) {
                found = breakfastOrder;
                break;
            }
        }
        if (found == null)
            throw new ResourceNotFoundException();

        return found;
    }

    public BreakfastOrder add(String stayPin, BreakfastOrderPOST entity) {
        BreakfastOrder breakfastOrder = new BreakfastOrder();

        Set<BreakfastOrderItem> entries = new HashSet<>();
        for (BreakfastOrderItemPOST entryPOST : entity.getItems()) {
            BreakfastOrderItem entry = new BreakfastOrderItem();
            BreakfastItem item = resolveItemIdToEntity(stayPin, entryPOST.getItemId());
            entry.setItem(item);
            entry.setCount(entryPOST.getCount());
            entries.add(entry);
        }
        breakfastOrder.setItems(entries);
        breakfastOrder.setTotal(computeTotal(entries));
        breakfastOrder.setTime(formatter.parseLocalDateTime(entity.getTime()));
        breakfastOrder.setStatus(OrderStatus.NEW);
        BreakfastOrder savedOrder = repository.save(breakfastOrder);

        Stay stay = stayService.get(stayPin);
        Set<BreakfastOrder> breakfastOrders = stay.getOrders().getBreakfastOrders();
        breakfastOrders.add(savedOrder);
        stayService.update(stayPin, stay);

        return savedOrder;
    }

    public BreakfastOrder addAndTryToNotify(String stayPin, BreakfastOrderPOST entity) {
        BreakfastOrder added = add(stayPin, entity);
        Stay stay = stayService.get(stayPin);

        eventPublisher.publishEvent(new NewOrderEvent(this, AvailableServiceType.BREAKFAST, stay));

        return added;
    }

    public BreakfastOrder update(String stayPin, Long id, BreakfastOrder updated) {
        BreakfastOrder order = get(stayPin, id);
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
        BreakfastOrder order = get(stayPin, id);
        order.setStatus(newStatus.getStatus());
        update(stayPin, order.getId(), order);
        return newStatus;
    }

    private BreakfastItem resolveItemIdToEntity(String stayPin, Long id) {
        Breakfast breakfast = stayService.get(stayPin).getHotel().getAvailableServices().getBreakfast();
        for (BreakfastCategory category : breakfast.getCategories()) {
            for (BreakfastItem item : category.getItems()) {
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

    private Price computeTotal(Set<BreakfastOrderItem> entries) {
        Price totalPrice = new Price();
        BigDecimal totalValue = BigDecimal.ZERO;

        for (BreakfastOrderItem entry : entries) {
            BreakfastItem item = entry.getItem();

            if (totalPrice.getCurrency() == null)
                totalPrice.setCurrency(item.getPrice().getCurrency());

            BigDecimal count = BigDecimal.valueOf(entry.getCount());
            totalValue = totalValue.add(item.getPrice().getValue().multiply(count));
        }

        totalPrice.setValue(totalValue);
        return totalPrice;
    }
}
