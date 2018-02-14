package com.horeca.site.services.orders;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.hotel.services.AvailableServiceType;
import com.horeca.site.models.hotel.services.spa.Spa;
import com.horeca.site.models.hotel.services.spa.SpaItem;
import com.horeca.site.models.hotel.services.spa.calendar.SpaCalendarDay;
import com.horeca.site.models.hotel.services.spa.calendar.SpaCalendarHour;
import com.horeca.site.models.orders.OrderStatus;
import com.horeca.site.models.orders.Orders;
import com.horeca.site.models.orders.spa.SpaOrder;
import com.horeca.site.models.orders.spa.SpaOrderPOST;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.repositories.orders.SpaOrderRepository;
import com.horeca.site.services.services.SpaService;
import com.horeca.site.services.services.StayService;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class SpaOrderService extends GenericOrderService<SpaOrder> {

    private final DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm");

    private OrdersService ordersService;
    private SpaService spaService;

    @Autowired
    public SpaOrderService(ApplicationEventPublisher eventPublisher,
                           SpaOrderRepository repository,
                           StayService stayService,
                           OrdersService ordersService,
                           SpaService spaService) {
        super(eventPublisher, repository, stayService);
        this.ordersService = ordersService;
        this.spaService = spaService;
    }

    public Set<SpaOrder> getAll(String stayPin) {
        Orders orders = ordersService.get(stayPin);
        return orders.getSpaOrders();
    }

    public SpaOrder add(String stayPin, SpaOrderPOST entity) {
        SpaOrder newOrder = new SpaOrder();

        SpaItem resolvedItem = resolveItemIdToEntity(stayPin, entity.getItemId());
        LocalDateTime reservationTime = formatter.parseLocalDateTime(entity.getTime());
        makeReservation(resolvedItem, reservationTime);

        newOrder.setStatus(OrderStatus.NEW);
        newOrder.setTime(reservationTime);
        newOrder.setItem(resolvedItem);
        SpaOrder savedOrder = repository.save(newOrder);

        Stay stay = stayService.get(stayPin);
        Set<SpaOrder> spaOrders = stay.getOrders().getSpaOrders();
        spaOrders.add(savedOrder);
        stayService.update(stayPin, stay);

        return savedOrder;
    }

    public SpaOrder addAndNotify(String stayPin, SpaOrderPOST entity) {
        SpaOrder added = add(stayPin, entity);
        notifyAboutNewOrder(stayPin, AvailableServiceType.SPA);

        return added;
    }

    private SpaItem resolveItemIdToEntity(String stayPin, Long id) {
        Spa spa = stayService.get(stayPin).getHotel().getAvailableServices().getSpa();
        for (SpaItem item : spa.getItems()) {
            if (item.getId().equals(id))
                return item;
        }

        throw new ResourceNotFoundException("Could not find an item with such an id");
    }

    private void makeReservation(SpaItem resolvedItem, LocalDateTime reservationTime) {
        Set<SpaCalendarDay> availableDays = resolvedItem.getCalendar().getDays();
        for (SpaCalendarDay day : availableDays) {
            if (day.getDay().equals(reservationTime.toLocalDate())) {

                Set<SpaCalendarHour> availableHours = day.getHours();
                for (SpaCalendarHour hour : availableHours) {
                    if (hour.getHour().equals(reservationTime.toLocalTime())) {
                        if (hour.isAvailable()) {
                            hour.setAvailable(false);
                            spaService.updateCalendarHour(hour);
                            return;
                        }
                        else
                            throw new BusinessRuleViolationException("The service is not available at the specified reservation time");
                    }
                }
            }
        }

        throw new BusinessRuleViolationException("The service is not available at the specified reservation time");
    }
}
