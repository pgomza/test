package com.horeca.site.controllers.orders;

import com.horeca.site.models.orders.OrderStatusPUT;
import com.horeca.site.models.orders.carpark.CarParkOrder;
import com.horeca.site.models.orders.carpark.CarParkOrderPOST;
import com.horeca.site.services.CarParkOrderService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "orders")
@RestController
@RequestMapping("/api/stays")
public class CarParkOrderController {

    @Autowired
    private CarParkOrderService service;

    @RequestMapping(value = "/{pin}/orders/carpark", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public CarParkOrder get(@PathVariable String pin) {
        return service.getActive(pin);
    }

    @RequestMapping(value = "/{pin}/orders/carpark", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public CarParkOrder add(@PathVariable String pin, @Valid @RequestBody CarParkOrderPOST newOrder) {
        return service.add(pin, newOrder);
    }

    @RequestMapping(value = "/{pin}/orders/carpark", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public CarParkOrder update(@PathVariable String pin, @Valid @RequestBody CarParkOrder updated) {
        return service.updateActive(pin, updated);
    }

    @RequestMapping(value = "/{pin}/orders/carpark/status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderStatusPUT getStatus(@PathVariable String pin) {
        return service.getActiveStatus(pin);
    }

    @RequestMapping(value = "/{pin}/orders/carpark/status", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderStatusPUT updateStatus(@PathVariable String pin, @Valid @RequestBody OrderStatusPUT newStatus) {
        return service.updateActiveStatus(pin, newStatus);
    }
}
