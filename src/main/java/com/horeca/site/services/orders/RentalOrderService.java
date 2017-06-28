package com.horeca.site.services.orders;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.Price;
import com.horeca.site.models.hotel.services.AvailableServiceType;
import com.horeca.site.models.hotel.services.rental.Rental;
import com.horeca.site.models.hotel.services.rental.RentalCategory;
import com.horeca.site.models.hotel.services.rental.RentalItem;
import com.horeca.site.models.notifications.NewOrderEvent;
import com.horeca.site.models.orders.OrderStatus;
import com.horeca.site.models.orders.OrderStatusPUT;
import com.horeca.site.models.orders.Orders;
import com.horeca.site.models.orders.rental.RentalOrder;
import com.horeca.site.models.orders.rental.RentalOrderItem;
import com.horeca.site.models.orders.rental.RentalOrderItemPOST;
import com.horeca.site.models.orders.rental.RentalOrderPOST;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.repositories.orders.RentalOrderRepository;
import com.horeca.site.services.services.StayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class RentalOrderService {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private StayService stayService;

    @Autowired
    private RentalOrderRepository repository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public Set<RentalOrder> getAll(String stayPin) {
        Orders orders = ordersService.get(stayPin);
        return orders.getRentalOrders();
    }

    public RentalOrder get(String stayPin, Long id) {
        RentalOrder found = null;
        for (RentalOrder order : getAll(stayPin)) {
            if (order.getId().equals(id)) {
                found = order;
                break;
            }
        }
        if (found == null)
            throw new ResourceNotFoundException();

        return found;
    }

    public RentalOrder add(String stayPin, RentalOrderPOST entity) {
        RentalOrder order = new RentalOrder();

        Set<RentalOrderItem> entries = new HashSet<>();
        for (RentalOrderItemPOST entryPOST : entity.getItems()) {
            RentalOrderItem entry = new RentalOrderItem();
            RentalItem item = resolveItemIdToEntity(stayPin, entryPOST.getItemId());
            entry.setItem(item);
            entry.setCount(entryPOST.getCount());
            entries.add(entry);
        }
        order.setItems(entries);
        order.setTime(entity.getTime());
        order.setTotal(computeTotal(entries));
        order.setStatus(OrderStatus.NEW);
        RentalOrder savedOrder = repository.save(order);

        Stay stay = stayService.get(stayPin);
        Set<RentalOrder> orders = stay.getOrders().getRentalOrders();
        orders.add(savedOrder);
        stayService.update(stayPin, stay);

        return savedOrder;
    }

    public RentalOrder addAndTryToNotify(String stayPin, RentalOrderPOST entity) {
        RentalOrder added = add(stayPin, entity);
        Stay stay = stayService.get(stayPin);

        eventPublisher.publishEvent(new NewOrderEvent(this, AvailableServiceType.RENTAL, stay));

        return added;
    }

    public RentalOrder update(String stayPin, Long id, RentalOrder updated) {
        RentalOrder order = get(stayPin, id);
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
        RentalOrder order = get(stayPin, id);
        order.setStatus(newStatus.getStatus());
        update(stayPin, order.getId(), order);
        return newStatus;
    }

    private RentalItem resolveItemIdToEntity(String stayPin, Long id) {
        Rental rental = stayService.get(stayPin).getHotel().getAvailableServices().getRental();
        for (RentalCategory category : rental.getCategories()) {
            for (RentalItem item : category.getItems()) {
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

    private Price computeTotal(Set<RentalOrderItem> entries) {
        Price totalPrice = new Price();
        BigDecimal totalValue = BigDecimal.ZERO;

        for (RentalOrderItem entry : entries) {
            RentalItem item = entry.getItem();

            if (totalPrice.getCurrency() == null)
                totalPrice.setCurrency(item.getPrice().getCurrency());

            BigDecimal count = BigDecimal.valueOf(entry.getCount());
            totalValue = totalValue.add(item.getPrice().getValue().multiply(count));
        }

        totalPrice.setValue(totalValue);
        return totalPrice;
    }
}
