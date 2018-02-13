package com.horeca.site.services.orders;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.hotel.services.AvailableServiceType;
import com.horeca.site.models.notifications.NewOrderEvent;
import com.horeca.site.models.orders.Order;
import com.horeca.site.models.orders.OrderStatus;
import com.horeca.site.models.orders.OrderStatusPUT;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.services.services.StayService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public abstract class GenericOrderServiceAltered<T extends Order> {

    private ApplicationEventPublisher eventPublisher;
    protected CrudRepository<T, Long> repository;
    protected StayService stayService;

    protected GenericOrderServiceAltered(ApplicationEventPublisher eventPublisher,
                                      CrudRepository<T, Long> repository,
                                      StayService stayService) {
        this.eventPublisher = eventPublisher;
        this.repository = repository;
        this.stayService = stayService;
    }

    protected abstract void ensureCanModifyOrders(String pin) throws BusinessRuleViolationException;

    public abstract Set<T> getAll(String stayPin);

    public T get(String stayPin, Long id) {
        Optional<T> orderOptional = getAll(stayPin).stream().filter(order -> order.getId().equals(id)).findAny();
        return orderOptional.orElseThrow(ResourceNotFoundException::new);
    }

    public T update(String pin, Long id, T updated) {
        ensureCanModifyOrders(pin);
        T order = get(pin, id);
        updated.setId(order.getId());
        return repository.save(updated);
    }

    public OrderStatusPUT getStatus(String pin, Long id) {
        OrderStatus status = get(pin, id).getStatus();
        OrderStatusPUT statusPUT = new OrderStatusPUT();
        statusPUT.setStatus(status);
        return statusPUT;
    }

    public OrderStatusPUT updateStatus(String pin, Long id, OrderStatusPUT newStatus) {
        ensureCanModifyOrders(pin);
        T order = get(pin, id);
        order.setStatus(newStatus.getStatus());
        update(pin, order.getId(), order);
        return newStatus;
    }

    protected void notifyAboutNewOrder(String pin, AvailableServiceType serviceType) {
        eventPublisher.publishEvent(new NewOrderEvent(this, serviceType, pin));
    }

    protected Long pinToHotelId(String pin) {
        Stay stay = stayService.get(pin);
        return stay.getHotel().getId();
    }
}
