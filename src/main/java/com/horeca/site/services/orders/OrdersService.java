package com.horeca.site.services.orders;

import com.horeca.site.models.orders.Orders;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.services.services.StayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrdersService {

    @Autowired
    private StayService stayService;

    public Orders get(String stayPin) {
        Stay stay = stayService.get(stayPin);
        Orders orders = stay.getOrders();

        return orders;
    }

    public OrdersView getView(String stayPin, String preferredLanguage) {
        String defaultLanguage = stayService.get(stayPin).getHotel().getDefaultTranslation();
        OrdersView ordersView = get(stayPin).toView(preferredLanguage, defaultLanguage);

        return ordersView;
    }
}
