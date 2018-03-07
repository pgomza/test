package com.horeca.site.services.orders;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.Price;
import com.horeca.site.models.hotel.services.AvailableServiceType;
import com.horeca.site.models.hotel.services.bar.Bar;
import com.horeca.site.models.hotel.services.bar.BarCategory;
import com.horeca.site.models.hotel.services.bar.BarItem;
import com.horeca.site.models.orders.Orders;
import com.horeca.site.models.orders.ServiceItemDataWithPrice;
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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class BarOrderService extends GenericOrderService<BarOrder> {

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
            throw new AccessDeniedException("The service is unavailable");
        }
    }

    public Set<BarOrder> getAll(String stayPin) {
        Orders orders = ordersService.get(stayPin);
        return orders.getBarOrders();
    }

    public BarOrder add(String pin, BarOrderPOST orderPOST) {
        ensureCanAddOrders(pin);

        Set<BarOrderItem> orderItemSet = new HashSet<>();
        for (BarOrderItemPOST entryPOST : orderPOST.getItems()) {
            BarItem serviceItem = resolveItemIdToEntity(pin, entryPOST.getItemId());
            // copy the data from item
            ServiceItemDataWithPrice itemData = new ServiceItemDataWithPrice(serviceItem.getName(), serviceItem.getPrice());
            BarOrderItem orderItem = new BarOrderItem(itemData, entryPOST.getCount());
            orderItemSet.add(orderItem);
        }
        Price totalPrice = computeTotal(orderItemSet);
        BarOrder order = new BarOrder(totalPrice, orderPOST.getTableNumber(), orderItemSet);
        BarOrder savedOrder = repository.save(order);

        Stay stay = stayService.get(pin);
        Set<BarOrder> roomServiceOrders = stay.getOrders().getBarOrders();
        roomServiceOrders.add(savedOrder);
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
                    return item;
                }
            }
        }
        throw new ResourceNotFoundException("Could not find an item with such an id");
    }

    private Price computeTotal(Set<BarOrderItem> entries) {
        Price totalPrice = new Price();
        BigDecimal totalValue = BigDecimal.ZERO;

        for (BarOrderItem entry : entries) {
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
