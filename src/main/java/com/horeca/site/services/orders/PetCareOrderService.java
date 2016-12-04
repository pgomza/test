package com.horeca.site.services.orders;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.hotel.services.petcare.PetCare;
import com.horeca.site.models.hotel.services.petcare.PetCareItem;
import com.horeca.site.models.hotel.services.petcare.calendar.PetCareCalendarDay;
import com.horeca.site.models.hotel.services.petcare.calendar.PetCareCalendarHour;
import com.horeca.site.models.hotel.services.spa.Spa;
import com.horeca.site.models.hotel.services.spa.SpaItem;
import com.horeca.site.models.hotel.services.spa.calendar.SpaCalendarDay;
import com.horeca.site.models.hotel.services.spa.calendar.SpaCalendarHour;
import com.horeca.site.models.orders.OrderStatus;
import com.horeca.site.models.orders.OrderStatusPUT;
import com.horeca.site.models.orders.Orders;
import com.horeca.site.models.orders.petcare.PetCareOrder;
import com.horeca.site.models.orders.petcare.PetCareOrderPOST;
import com.horeca.site.models.orders.petcare.PetCareOrderView;
import com.horeca.site.models.orders.spa.SpaOrder;
import com.horeca.site.models.orders.spa.SpaOrderPOST;
import com.horeca.site.models.orders.spa.SpaOrderView;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.repositories.orders.PetCareOrderRepository;
import com.horeca.site.repositories.orders.SpaOrderRepository;
import com.horeca.site.services.services.PetCareService;
import com.horeca.site.services.services.StayService;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class PetCareOrderService {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private StayService stayService;

    @Autowired
    private PetCareService petCareService;

    @Autowired
    private PetCareOrderRepository repository;

    private DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm");

    public PetCareOrder get(String stayPin, Long id) {
        PetCareOrder found = null;
        for (PetCareOrder petCareOrder : getAll(stayPin)) {
            if (petCareOrder.getId().equals(id)) {
                found = petCareOrder;
                break;
            }
        }
        if (found == null)
            throw new ResourceNotFoundException();

        return found;
    }

    public Set<PetCareOrder> getAll(String stayPin) {
        Orders orders = ordersService.get(stayPin);
        Set<PetCareOrder> petCareOrders = orders.getPetCareOrders();

        return petCareOrders;
    }

    public PetCareOrderView getView(String stayPin, Long id, String preferredLanguage) {
        String defaultLanguage = stayService.get(stayPin).getHotel().getDefaultTranslation();
        return get(stayPin, id).toView(preferredLanguage, defaultLanguage);
    }

    public Set<PetCareOrderView> getAllViews(String stayPin, String preferredLanguage) {
        String defaultLanguage = stayService.get(stayPin).getHotel().getDefaultTranslation();
        Set<PetCareOrderView> views = new HashSet<>();
        for (PetCareOrder petCareOrder : getAll(stayPin)) {
            views.add(petCareOrder.toView(preferredLanguage, defaultLanguage));
        }
        return views;
    }

    public PetCareOrder add(String stayPin, PetCareOrderPOST entity) {
        PetCareOrder newOrder = new PetCareOrder();

        PetCareItem resolvedItem = resolveItemIdToEntity(stayPin, entity.getItemId());
        LocalDateTime reservationTime = formatter.parseLocalDateTime(entity.getTime());
        makeReservation(resolvedItem, reservationTime);

        newOrder.setStatus(OrderStatus.NEW);
        newOrder.setTime(reservationTime);
        newOrder.setItem(resolvedItem);
        PetCareOrder savedOrder = repository.save(newOrder);

        Stay stay = stayService.get(stayPin);
        Set<PetCareOrder> petCareOrders = stay.getOrders().getPetCareOrders();
        petCareOrders.add(savedOrder);
        stayService.update(stayPin, stay);

        return savedOrder;
    }


    public PetCareOrder update(String stayPin, Long id, PetCareOrder updated) {
        PetCareOrder order = get(stayPin, id);
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
        PetCareOrder order = get(stayPin, id);
        order.setStatus(newStatus.getStatus());
        update(stayPin, order.getId(), order);
        return newStatus;
    }

    private PetCareItem resolveItemIdToEntity(String stayPin, Long id) {
        PetCare petCare = stayService.get(stayPin).getHotel().getAvailableServices().getPetCare();
        for (PetCareItem item : petCare.getItems()) {
            if (item.getId().equals(id))
                return item;
        }

        throw new ResourceNotFoundException("Could not find an item with such an id");
    }

    private void makeReservation(PetCareItem resolvedItem, LocalDateTime reservationTime) {
        Set<PetCareCalendarDay> availableDays = resolvedItem.getCalendar().getDays();
        for (PetCareCalendarDay day : availableDays) {
            if (day.getDay().equals(reservationTime.toLocalDate())) {

                Set<PetCareCalendarHour> availableHours = day.getHours();
                for (PetCareCalendarHour hour : availableHours) {
                    if (hour.getHour().equals(reservationTime.toLocalTime())) {
                        if (hour.isAvailable()) {
                            hour.setAvailable(false);
                            petCareService.updateCalendarHour(hour);
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
