package com.horeca.site.services.orders;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.models.hotel.services.AvailableServiceType;
import com.horeca.site.models.hotel.services.tableordering.TableOrdering;
import com.horeca.site.models.orders.Orders;
import com.horeca.site.models.orders.tableordering.TableOrderingOrder;
import com.horeca.site.models.orders.tableordering.TableOrderingOrderPOST;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.repositories.orders.TableOrderingOrderRepository;
import com.horeca.site.services.services.StayService;
import com.horeca.site.services.services.TableOrderingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class TableOrderingOrderService extends GenericOrderService<TableOrderingOrder> {

    private TableOrderingService tableOrderingService;
    private OrdersService ordersService;

    @Autowired
    public TableOrderingOrderService(ApplicationEventPublisher eventPublisher,
                                     TableOrderingOrderRepository repository,
                                     StayService stayService,
                                     TableOrderingService tableOrderingService,
                                     OrdersService ordersService) {
        super(eventPublisher, repository, stayService);
        this.tableOrderingService = tableOrderingService;
        this.ordersService = ordersService;
    }

    private void ensureCanAddOrders(String pin) {
        Long hotelId = pinToHotelId(pin);
        TableOrdering tableOrdering = tableOrderingService.get(hotelId);
        if (!tableOrdering.getAvailable()) {
            throw new BusinessRuleViolationException("The service is unavailable");
        }
    }

    public Set<TableOrderingOrder> getAll(String stayPin) {
        Orders orders = ordersService.get(stayPin);
        return orders.getTableOrderingOrders();
    }

    public TableOrderingOrder add(String pin, TableOrderingOrderPOST entity) {
        ensureCanAddOrders(pin);

        TableOrderingOrder newOrder = new TableOrderingOrder();
        newOrder.setTime(entity.time);
        newOrder.setNumberOfPeople(entity.numberOfPeople);
        TableOrderingOrder savedOrder = repository.save(newOrder);

        Stay stay = stayService.get(pin);
        Set<TableOrderingOrder> orders = stay.getOrders().getTableOrderingOrders();
        orders.add(savedOrder);
        stayService.update(pin, stay);

        return savedOrder;
    }

    public TableOrderingOrder addAndNotify(String pin, TableOrderingOrderPOST entity) {
        ensureCanAddOrders(pin);

        TableOrderingOrder added = add(pin, entity);
        notifyAboutNewOrder(pin, AvailableServiceType.TABLEORDERING);

        return added;
    }
}
