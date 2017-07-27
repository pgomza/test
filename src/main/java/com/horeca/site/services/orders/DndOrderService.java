package com.horeca.site.services.orders;

import com.horeca.site.models.orders.Orders;
import com.horeca.site.models.orders.dnd.DndOrder;
import com.horeca.site.models.orders.dnd.DndOrderUPDATE;
import com.horeca.site.repositories.orders.DndOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DndOrderService {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private DndOrderRepository repository;

    public DndOrder get(String stayPin) {
        Orders orders = ordersService.get(stayPin);
        return orders.getDnd();
    }

    public DndOrder update(String stayPin, DndOrderUPDATE updated) {
        DndOrder oldOne = get(stayPin);
        oldOne.setStatus(updated.getStatus());
        return repository.save(oldOne);
    }
}
