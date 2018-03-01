package com.horeca.site.services.orders;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.Price;
import com.horeca.site.models.hotel.services.AvailableServiceType;
import com.horeca.site.models.hotel.services.rental.Rental;
import com.horeca.site.models.hotel.services.rental.RentalCategory;
import com.horeca.site.models.hotel.services.rental.RentalItem;
import com.horeca.site.models.orders.Orders;
import com.horeca.site.models.orders.ServiceItemDataWithPrice;
import com.horeca.site.models.orders.rental.RentalOrder;
import com.horeca.site.models.orders.rental.RentalOrderItem;
import com.horeca.site.models.orders.rental.RentalOrderItemPOST;
import com.horeca.site.models.orders.rental.RentalOrderPOST;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.repositories.orders.RentalOrderRepository;
import com.horeca.site.services.services.StayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class RentalOrderService extends GenericOrderService<RentalOrder> {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private StayService stayService;

    @Autowired
    private RentalOrderRepository repository;

    @Override
    protected CrudRepository<RentalOrder, Long> getRepository() {
        return repository;
    }

    public Set<RentalOrder> getAll(String stayPin) {
        Orders orders = ordersService.get(stayPin);
        return orders.getRentalOrders();
    }

    public RentalOrder add(String stayPin, RentalOrderPOST entity) {
        Set<RentalOrderItem> orderItemSet = new HashSet<>();
        for (RentalOrderItemPOST entryPOST : entity.getItems()) {
            RentalItem serviceItem = resolveItemIdToEntity(stayPin, entryPOST.getItemId());
            // copy the data from item
            ServiceItemDataWithPrice itemData = new ServiceItemDataWithPrice(serviceItem.getName(), serviceItem.getPrice());
            RentalOrderItem orderItem = new RentalOrderItem(itemData, entryPOST.getCount());
            orderItemSet.add(orderItem);
        }
        Price totalPrice = computeTotal(orderItemSet);
        RentalOrder order = new RentalOrder(totalPrice, orderItemSet, entity.getTime());
        RentalOrder savedOrder = repository.save(order);

        Stay stay = stayService.get(stayPin);
        Set<RentalOrder> orders = stay.getOrders().getRentalOrders();
        orders.add(savedOrder);
        stayService.update(stayPin, stay);

        return savedOrder;
    }

    public RentalOrder addAndNotify(String stayPin, RentalOrderPOST entity) {
        RentalOrder added = add(stayPin, entity);
        notifyAboutNewOrder(stayPin, AvailableServiceType.RENTAL);

        return added;
    }

    private RentalItem resolveItemIdToEntity(String stayPin, Long id) {
        Rental rental = stayService.get(stayPin).getHotel().getAvailableServices().getRental();
        for (RentalCategory category : rental.getCategories()) {
            for (RentalItem item : category.getItems()) {
                if (item.getId().equals(id)) {
                    return item;
                }
            }
        }
        throw new ResourceNotFoundException("Could not find an item with such an id");
    }

    private Price computeTotal(Set<RentalOrderItem> entries) {
        Price totalPrice = new Price();
        BigDecimal totalValue = BigDecimal.ZERO;

        for (RentalOrderItem entry : entries) {
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
