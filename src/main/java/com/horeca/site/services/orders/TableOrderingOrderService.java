package com.horeca.site.services.orders;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.orders.OrderStatus;
import com.horeca.site.models.orders.OrderStatusPUT;
import com.horeca.site.models.orders.Orders;
import com.horeca.site.models.orders.tableordering.TableOrderingOrder;
import com.horeca.site.models.orders.tableordering.TableOrderingOrderPOST;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.repositories.orders.TableOrderingOrderRepository;
import com.horeca.site.services.services.StayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class TableOrderingOrderService {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private StayService stayService;

    @Autowired
    private TableOrderingOrderRepository repository;

    public Set<TableOrderingOrder> getAll(String stayPin) {
        Orders orders = ordersService.get(stayPin);
        Set<TableOrderingOrder> tableOrderingOrders = orders.getTableOrderingOrders();
        return tableOrderingOrders;
    }

    public TableOrderingOrder get(String stayPin, Long id) {
        Optional<TableOrderingOrder> matchedOrder = getAll(stayPin).stream()
                .filter(order -> order.getId().equals(id))
                .findFirst();

        if (matchedOrder.isPresent())
            return matchedOrder.get();
        else
            throw new ResourceNotFoundException();
    }

    public TableOrderingOrder add(String stayPin, TableOrderingOrderPOST entity) {
        TableOrderingOrder newOrder = new TableOrderingOrder(entity.time, entity.numberOfPeople);
        TableOrderingOrder savedOrder = repository.save(newOrder);

        Stay stay = stayService.get(stayPin);
        Set<TableOrderingOrder> orders = stay.getOrders().getTableOrderingOrders();
        orders.add(savedOrder);
        stayService.update(stayPin, stay);

        return savedOrder;
    }

    public TableOrderingOrder update(String stayPin, Long id, TableOrderingOrder updated) {
        TableOrderingOrder order = get(stayPin, id);
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
        TableOrderingOrder order = get(stayPin, id);
        order.setStatus(newStatus.getStatus());
        update(stayPin, order.getId(), order);
        return newStatus;
    }
}
