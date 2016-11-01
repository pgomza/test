package com.horeca.site.controllers.orders;

import com.horeca.annotations.AllowCORS;
import com.horeca.site.models.orders.OrdersView;
import com.horeca.site.services.OrdersService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(value = "orders")
@AllowCORS
@RestController
@RequestMapping("/api/stays")
public class OrdersController {

    @Autowired
    private OrdersService service;

    @RequestMapping(value = "/{pin}/orders", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public OrdersView get(@PathVariable("pin") String pin, HttpServletRequest request) {
        String preferredLanguage = request.getLocale().getLanguage();
        return service.getView(pin, preferredLanguage);
    }
}
