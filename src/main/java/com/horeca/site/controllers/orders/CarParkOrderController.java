package com.horeca.site.controllers.orders;

import com.horeca.site.models.orders.OrderStatusPUT;
import com.horeca.site.models.orders.carpark.CarParkOrder;
import com.horeca.site.models.orders.carpark.CarParkOrderPOST;
import com.horeca.site.services.orders.CarParkOrderService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@Api(value = "orders")
@RestController
@RequestMapping("/api/stays")
public class CarParkOrderController {

    @Autowired
    private CarParkOrderService service;

    @RequestMapping(value = "/{pin}/orders/carpark", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<CarParkOrder> getAll(@PathVariable String pin) {
        return service.getAll(pin);
    }

    @RequestMapping(value = "/{pin}/orders/carpark/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public CarParkOrder get(@PathVariable String pin, @PathVariable Long id) {
        return service.get(pin, id);
    }

    @RequestMapping(value = "/{pin}/orders/carpark", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public CarParkOrder add(@PathVariable String pin, @Valid @RequestBody CarParkOrderPOST newOrder) {
        return service.add(pin, newOrder);
    }

    @RequestMapping(value = "/{pin}/orders/carpark/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public CarParkOrder update(@PathVariable String pin, @PathVariable Long id, @Valid @RequestBody CarParkOrder updated) {
        return service.update(pin, id, updated);
    }

    @RequestMapping(value = "/{pin}/orders/carpark/{id}/status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderStatusPUT getStatus(@PathVariable String pin, @PathVariable Long id) {
        return service.getStatus(pin, id);
    }

    @RequestMapping(value = "/{pin}/orders/carpark/{id}/status", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderStatusPUT updateStatus(@PathVariable String pin, @PathVariable Long id, @Valid @RequestBody OrderStatusPUT newStatus) {
        return service.updateStatus(pin, id, newStatus);
    }
}
