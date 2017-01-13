package com.horeca.site.services.orders;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.orders.Order;
import com.horeca.site.models.orders.OrderStatus;
import com.horeca.site.models.orders.OrderStatusPUT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public abstract class GenericOrderService<T extends Order> {

    @Autowired
    protected CrudRepository<T, Long> repository;

    public abstract Set<T> getAll(String stayPin);

    public T get(String stayPin, Long id) {
        T found = null;
        for (T order : getAll(stayPin)) {
            if (order.getId().equals(id)) {
                found = order;
                break;
            }
        }
        if (found == null)
            throw new ResourceNotFoundException();

        return found;
    }

    public T update(String stayPin, Long id, T updated) {
        T order = get(stayPin, id);
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
        T order = get(stayPin, id);
        order.setStatus(newStatus.getStatus());
        update(stayPin, order.getId(), order);
        return newStatus;
    }
}
