package com.horeca.site.services.orders;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.Price;
import com.horeca.site.models.hotel.services.AvailableServiceType;
import com.horeca.site.models.hotel.services.breakfast.Breakfast;
import com.horeca.site.models.hotel.services.breakfast.BreakfastCategory;
import com.horeca.site.models.hotel.services.breakfast.BreakfastItem;
import com.horeca.site.models.orders.OrderStatus;
import com.horeca.site.models.orders.Orders;
import com.horeca.site.models.orders.breakfast.BreakfastOrder;
import com.horeca.site.models.orders.breakfast.BreakfastOrderItem;
import com.horeca.site.models.orders.breakfast.BreakfastOrderItemPOST;
import com.horeca.site.models.orders.breakfast.BreakfastOrderPOST;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.repositories.orders.BreakfastOrderRepository;
import com.horeca.site.services.services.BreakfastService;
import com.horeca.site.services.services.StayService;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class BreakfastOrderService extends GenericOrderService<BreakfastOrder> {

    private final DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm");

    private BreakfastService breakfastService;
    private OrdersService ordersService;

    @Autowired
    public BreakfastOrderService(ApplicationEventPublisher eventPublisher,
                                 BreakfastOrderRepository repository,
                                 BreakfastService breakfastService,
                                 StayService stayService,
                                 OrdersService ordersService) {
        super(eventPublisher, repository, stayService);
        this.breakfastService = breakfastService;
        this.ordersService = ordersService;
    }

    private void ensureCanAddOrders(String pin) {
        Long hotelId = pinToHotelId(pin);
        Breakfast breakfast = breakfastService.get(hotelId);
        if (!breakfast.getAvailable()) {
            throw new AccessDeniedException("The service is unavailable");
        }
    }

    public Set<BreakfastOrder> getAll(String stayPin) {
        Orders orders = ordersService.get(stayPin);
        return orders.getBreakfastOrders();
    }

    public BreakfastOrder add(String pin, BreakfastOrderPOST entity) {
        ensureCanAddOrders(pin);

        BreakfastOrder breakfastOrder = new BreakfastOrder();

        Set<BreakfastOrderItem> entries = new HashSet<>();
        for (BreakfastOrderItemPOST entryPOST : entity.getItems()) {
            BreakfastOrderItem entry = new BreakfastOrderItem();
            BreakfastItem item = resolveItemIdToEntity(pin, entryPOST.getItemId());
            entry.setItem(item);
            entry.setCount(entryPOST.getCount());
            entries.add(entry);
        }
        breakfastOrder.setItems(entries);
        breakfastOrder.setTotal(computeTotal(entries));
        breakfastOrder.setTime(formatter.parseLocalDateTime(entity.getTime()));
        breakfastOrder.setStatus(OrderStatus.NEW);
        BreakfastOrder savedOrder = repository.save(breakfastOrder);

        Stay stay = stayService.get(pin);
        Set<BreakfastOrder> breakfastOrders = stay.getOrders().getBreakfastOrders();
        breakfastOrders.add(savedOrder);
        stayService.update(pin, stay);

        return savedOrder;
    }

    public BreakfastOrder addAndNotify(String pin, BreakfastOrderPOST entity) {
        ensureCanAddOrders(pin);

        BreakfastOrder added = add(pin, entity);
        notifyAboutNewOrder(pin, AvailableServiceType.BREAKFAST);

        return added;
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
