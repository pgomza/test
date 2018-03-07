package com.horeca.site.services.orders;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.Price;
import com.horeca.site.models.hotel.services.AvailableServiceType;
import com.horeca.site.models.hotel.services.roomservice.RoomService;
import com.horeca.site.models.hotel.services.roomservice.RoomServiceCategory;
import com.horeca.site.models.hotel.services.roomservice.RoomServiceItem;
import com.horeca.site.models.orders.Orders;
import com.horeca.site.models.orders.ServiceItemDataWithPrice;
import com.horeca.site.models.orders.roomservice.RoomServiceOrder;
import com.horeca.site.models.orders.roomservice.RoomServiceOrderItem;
import com.horeca.site.models.orders.roomservice.RoomServiceOrderItemPOST;
import com.horeca.site.models.orders.roomservice.RoomServiceOrderPOST;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.repositories.orders.RoomServiceOrderRepository;
import com.horeca.site.services.services.RoomServiceService;
import com.horeca.site.services.services.StayService;
import org.joda.time.LocalTime;
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
public class RoomServiceOrderService extends GenericOrderService<RoomServiceOrder> {

    private final DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm");

    private RoomServiceService roomServiceService;
    private OrdersService ordersService;

    @Autowired
    public RoomServiceOrderService(ApplicationEventPublisher eventPublisher,
                                   RoomServiceOrderRepository repository,
                                   StayService stayService,
                                   RoomServiceService roomServiceService,
                                   OrdersService ordersService) {
        super(eventPublisher, repository, stayService);
        this.roomServiceService = roomServiceService;
        this.ordersService = ordersService;
    }

    private void ensureCanAddOrders(String pin) {
        Long hotelId = pinToHotelId(pin);
        RoomService roomService = roomServiceService.get(hotelId);
        if (!roomService.getAvailable()) {
            throw new AccessDeniedException("The service is unavailable");
        }
    }

    @Override
    public Set<RoomServiceOrder> getAll(String stayPin) {
        Orders orders = ordersService.get(stayPin);
        return orders.getRoomServiceOrders();
    }

    public RoomServiceOrder add(String pin, RoomServiceOrderPOST orderPOST) {
        ensureCanAddOrders(pin);

        Set<RoomServiceOrderItem> orderItemSet = new HashSet<>();
        for (RoomServiceOrderItemPOST entryPOST : orderPOST.getItems()) {
            RoomServiceItem serviceItem = resolveItemIdToEntity(pin, entryPOST.getItemId());
            // copy the data from item
            ServiceItemDataWithPrice itemData = new ServiceItemDataWithPrice(serviceItem.getName(), serviceItem.getPrice());
            RoomServiceOrderItem orderItem = new RoomServiceOrderItem(itemData, entryPOST.getCount());
            orderItemSet.add(orderItem);
        }
        LocalTime orderTime = formatter.parseLocalTime(orderPOST.getTime());
        Price totalPrice = computeTotal(orderItemSet);
        RoomServiceOrder order = new RoomServiceOrder(totalPrice, orderTime, orderItemSet);
        RoomServiceOrder savedOrder = repository.save(order);

        Stay stay = stayService.get(pin);
        Set<RoomServiceOrder> roomServiceOrders = stay.getOrders().getRoomServiceOrders();
        roomServiceOrders.add(savedOrder);
        stayService.update(pin, stay);

        return savedOrder;
    }

    public RoomServiceOrder addAndNotify(String pin, RoomServiceOrderPOST entity) {
        ensureCanAddOrders(pin);

        RoomServiceOrder added = add(pin, entity);
        notifyAboutNewOrder(pin, AvailableServiceType.ROOMSERVICE);

        return added;
    }

    private RoomServiceItem resolveItemIdToEntity(String stayPin, Long id) {
        RoomService roomService = stayService.get(stayPin).getHotel().getAvailableServices().getRoomService();
        for (RoomServiceCategory category : roomService.getCategories()) {
            for (RoomServiceItem item : category.getItems()) {
                if (item.getId().equals(id)) {
                    return item;
                }
            }
        }
        throw new ResourceNotFoundException("Could not find an item with such an id");
    }

    private Price computeTotal(Set<RoomServiceOrderItem> entries) {
        Price totalPrice = new Price();
        BigDecimal totalValue = BigDecimal.ZERO;

        for (RoomServiceOrderItem entry : entries) {
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
