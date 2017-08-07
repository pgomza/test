package com.horeca.site.services.orders;

import com.horeca.site.models.hotel.services.AvailableServiceType;
import com.horeca.site.models.orders.Orders;
import com.horeca.site.models.orders.tableordering.TableOrderingOrder;
import com.horeca.site.models.orders.tableordering.TableOrderingOrderPOST;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.repositories.orders.TableOrderingOrderRepository;
import com.horeca.site.services.services.StayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class TableOrderingOrderService extends GenericOrderService<TableOrderingOrder> {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private StayService stayService;

    @Autowired
    private TableOrderingOrderRepository repository;

    @Override
    protected CrudRepository<TableOrderingOrder, Long> getRepository() {
        return repository;
    }

    public Set<TableOrderingOrder> getAll(String stayPin) {
        Orders orders = ordersService.get(stayPin);
        return orders.getTableOrderingOrders();
    }

    public TableOrderingOrder add(String stayPin, TableOrderingOrderPOST entity) {
        TableOrderingOrder newOrder = new TableOrderingOrder();
        newOrder.setTime(entity.time);
        newOrder.setNumberOfPeople(entity.numberOfPeople);
        TableOrderingOrder savedOrder = repository.save(newOrder);

        Stay stay = stayService.get(stayPin);
        Set<TableOrderingOrder> orders = stay.getOrders().getTableOrderingOrders();
        orders.add(savedOrder);
        stayService.update(stayPin, stay);

        return savedOrder;
    }

    public TableOrderingOrder addAndNotify(String stayPin, TableOrderingOrderPOST entity) {
        TableOrderingOrder added = add(stayPin, entity);
        notifyAboutNewOrder(stayPin, AvailableServiceType.TABLEORDERING);

        return added;
    }
}
