package com.horeca.site.services;

import com.horeca.site.models.orders.Orders;
import com.horeca.site.models.stay.Stay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrdersService {

    @Autowired
    private StayService stayService;

    public Orders getOrders(String stayPin) {
        Stay stay = stayService.get(stayPin);
        Orders orders = stay.getOrders();

        return orders;
    }
}
