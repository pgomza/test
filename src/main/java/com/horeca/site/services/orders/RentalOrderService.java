package com.horeca.site.services.orders;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.Price;
import com.horeca.site.models.hotel.services.AvailableServiceType;
import com.horeca.site.models.hotel.services.rental.Rental;
import com.horeca.site.models.hotel.services.rental.RentalCategory;
import com.horeca.site.models.hotel.services.rental.RentalItem;
import com.horeca.site.models.orders.OrderStatus;
import com.horeca.site.models.orders.Orders;
import com.horeca.site.models.orders.rental.RentalOrder;
import com.horeca.site.models.orders.rental.RentalOrderItem;
import com.horeca.site.models.orders.rental.RentalOrderItemPOST;
import com.horeca.site.models.orders.rental.RentalOrderPOST;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.repositories.orders.RentalOrderRepository;
import com.horeca.site.services.services.RentalService;
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
public class RentalOrderService extends GenericOrderService<RentalOrder> {

    private RentalService rentalService;
    private OrdersService ordersService;

    @Autowired
    public RentalOrderService(ApplicationEventPublisher eventPublisher,
                              RentalOrderRepository repository,
                              StayService stayService,
                              RentalService rentalService,
                              OrdersService ordersService) {
        super(eventPublisher, repository, stayService);
        this.rentalService = rentalService;
        this.ordersService = ordersService;
    }

    private void ensureCanAddOrders(String pin) {
        Long hotelId = pinToHotelId(pin);
        Rental rental = rentalService.get(hotelId);
        if (!rental.getAvailable()) {
            throw new AccessDeniedException("The service is unavailable");
        }
    }

    public Set<RentalOrder> getAll(String stayPin) {
        Orders orders = ordersService.get(stayPin);
        return orders.getRentalOrders();
    }

    public RentalOrder add(String pin, RentalOrderPOST entity) {
        ensureCanAddOrders(pin);

        RentalOrder order = new RentalOrder();

        Set<RentalOrderItem> entries = new HashSet<>();
        for (RentalOrderItemPOST entryPOST : entity.getItems()) {
            RentalOrderItem entry = new RentalOrderItem();
            RentalItem item = resolveItemIdToEntity(pin, entryPOST.getItemId());
            entry.setItem(item);
            entry.setCount(entryPOST.getCount());
            entries.add(entry);
        }
        order.setItems(entries);
        order.setTime(entity.getTime());
        order.setTotal(computeTotal(entries));
        order.setStatus(OrderStatus.NEW);
        RentalOrder savedOrder = repository.save(order);

        Stay stay = stayService.get(pin);
        Set<RentalOrder> orders = stay.getOrders().getRentalOrders();
        orders.add(savedOrder);
        stayService.update(pin, stay);

        return savedOrder;
    }

    public RentalOrder addAndNotify(String pin, RentalOrderPOST entity) {
        ensureCanAddOrders(pin);

        RentalOrder added = add(pin, entity);
        notifyAboutNewOrder(pin, AvailableServiceType.RENTAL);

        return added;
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
