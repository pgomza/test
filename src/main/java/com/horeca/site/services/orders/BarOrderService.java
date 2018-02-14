package com.horeca.site.services.orders;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.Price;
import com.horeca.site.models.hotel.services.AvailableServiceType;
import com.horeca.site.models.hotel.services.bar.Bar;
import com.horeca.site.models.hotel.services.bar.BarCategory;
import com.horeca.site.models.hotel.services.bar.BarItem;
import com.horeca.site.models.orders.OrderStatus;
import com.horeca.site.models.orders.Orders;
import com.horeca.site.models.orders.bar.BarOrder;
import com.horeca.site.models.orders.bar.BarOrderItem;
import com.horeca.site.models.orders.bar.BarOrderItemPOST;
import com.horeca.site.models.orders.bar.BarOrderPOST;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.repositories.orders.BarOrderRepository;
import com.horeca.site.services.services.BarService;
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
public class BarOrderService extends GenericOrderServiceAltered<BarOrder> {

    private BarService barService;
    private OrdersService ordersService;

    @Autowired
    public BarOrderService(ApplicationEventPublisher eventPublisher,
                           BarOrderRepository repository,
                           StayService stayService,
                           BarService barService,
                           OrdersService ordersService) {
        super(eventPublisher, repository, stayService);
        this.barService = barService;
        this.ordersService = ordersService;
    }

    private void ensureCanAddOrders(String pin) {
        Long hotelId = pinToHotelId(pin);
        Bar bar = barService.get(hotelId);
        if (!bar.getAvailable()) {
            throw new BusinessRuleViolationException("The service is unavailable");
        }
    }

    public Set<BarOrder> getAll(String stayPin) {
        Orders orders = ordersService.get(stayPin);
        return orders.getBarOrders();
    }

    public BarOrder add(String pin, BarOrderPOST entity) {
        ensureCanAddOrders(pin);

        BarOrder barOrder = new BarOrder();

        Set<BarOrderItem> entries = new HashSet<>();
        for (BarOrderItemPOST entryPOST : entity.getItems()) {
            BarOrderItem entry = new BarOrderItem();
            BarItem item = resolveItemIdToEntity(pin, entryPOST.getItemId());
            entry.setItem(item);
            entry.setCount(entryPOST.getCount());
            entries.add(entry);
        }
        barOrder.setTableNumber(entity.getTableNumber());
        barOrder.setItems(entries);
        barOrder.setTotal(computeTotal(entries));
        barOrder.setStatus(OrderStatus.NEW);
        BarOrder savedOrder = repository.save(barOrder);

        Stay stay = stayService.get(pin);
        Set<BarOrder> barOrders = stay.getOrders().getBarOrders();
        barOrders.add(savedOrder);
        stayService.update(pin, stay);

        return savedOrder;
    }

    public BarOrder addAndNotify(String pin, BarOrderPOST entity) {
        ensureCanAddOrders(pin);

        BarOrder added = add(pin, entity);
        notifyAboutNewOrder(pin, AvailableServiceType.BAR);

        return added;
    }

    private BarItem resolveItemIdToEntity(String stayPin, Long id) {
        Bar bar = stayService.get(stayPin).getHotel().getAvailableServices().getBar();
        for (BarCategory category : bar.getCategories()) {
            for (BarItem item : category.getItems()) {
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

    private Price computeTotal(Set<BarOrderItem> entries) {
        Price totalPrice = new Price();
        BigDecimal totalValue = BigDecimal.ZERO;

        for (BarOrderItem entry : entries) {
            BarItem item = entry.getItem();

            if (totalPrice.getCurrency() == null)
                totalPrice.setCurrency(item.getPrice().getCurrency());

            BigDecimal count = BigDecimal.valueOf(entry.getCount());
            totalValue = totalValue.add(item.getPrice().getValue().multiply(count));
        }

        totalPrice.setValue(totalValue);
        return totalPrice;
    }
}
