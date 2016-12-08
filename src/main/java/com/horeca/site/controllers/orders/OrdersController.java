package com.horeca.site.controllers.orders;

import com.horeca.annotations.AllowCORS;
import com.horeca.site.models.orders.Orders;
import com.horeca.site.services.orders.OrdersService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Api(value = "orders")
@AllowCORS
@RestController
@RequestMapping("/api/stays")
public class OrdersController {

    @Autowired
    private OrdersService service;

    @RequestMapping(value = "/{pin}/orders", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Orders get(@PathVariable("pin") String pin) {
        return service.get(pin);
    }
}
