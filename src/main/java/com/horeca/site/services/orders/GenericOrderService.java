package com.horeca.site.services.orders;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.hotel.services.AvailableServiceType;
import com.horeca.site.models.notifications.NewOrderEvent;
import com.horeca.site.models.orders.Order;
import com.horeca.site.models.orders.OrderStatus;
import com.horeca.site.models.orders.OrderStatusPUT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public abstract class GenericOrderService<T extends Order> {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    protected abstract CrudRepository<T, Long> getRepository();

    public abstract Set<T> getAll(String stayPin);

    public T get(String stayPin, Long id) {
        Optional<T> orderOptional = getAll(stayPin).stream().filter(order -> order.getId().equals(id)).findAny();
        return orderOptional.orElseThrow(ResourceNotFoundException::new);
    }

    public T update(String stayPin, Long id, T updated) {
        T order = get(stayPin, id);
        updated.setId(order.getId());
        return getRepository().save(updated);
    }

    public OrderStatusPUT getStatus(String pin, Long id) {
        OrderStatus status = get(pin, id).getStatus();
        OrderStatusPUT statusPUT = new OrderStatusPUT();
        statusPUT.setStatus(status);
        return statusPUT;
    }

    public OrderStatusPUT updateStatus(String stayPin, Long id, OrderStatusPUT newStatus) {
        T order = get(stayPin, id);
        order.setStatus(newStatus.getStatus());
        update(stayPin, order.getId(), order);
        return newStatus;
    }

    protected void notifyAboutNewOrder(String stayPin, AvailableServiceType serviceType) {
        eventPublisher.publishEvent(new NewOrderEvent(this, serviceType, stayPin));
    }
}
