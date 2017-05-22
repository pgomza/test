package com.horeca.site.controllers.orders;

import com.horeca.site.models.orders.OrderStatusPUT;
import com.horeca.site.models.orders.bar.BarOrder;
import com.horeca.site.models.orders.bar.BarOrderPOST;
import com.horeca.site.security.models.GuestAccount;
import com.horeca.site.services.orders.BarOrderService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@Api(value = "orders")
@RestController
@RequestMapping("/api/stays")
public class BarOrderController {

    @Autowired
    private BarOrderService service;

    @RequestMapping(value = "/{pin}/orders/bar", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<BarOrder> getAll(@PathVariable String pin) {
        return service.getAll(pin);
    }

    @RequestMapping(value = "/{pin}/orders/bar/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public BarOrder get(@PathVariable String pin, @PathVariable Long id) {
        return service.get(pin, id);
    }

    @RequestMapping(value = "/{pin}/orders/bar", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public BarOrder add(@PathVariable String pin, @Valid @RequestBody BarOrderPOST newOrder,
                              Authentication authentication) {
        if (authentication.getPrincipal() instanceof GuestAccount) {
            return service.addAndTryToNotify(pin, newOrder);
        }
        else
            return service.add(pin, newOrder);
    }

    @RequestMapping(value = "/{pin}/orders/bar/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public BarOrder update(@PathVariable String pin, @PathVariable Long id, @Valid @RequestBody BarOrder updated) {
        return service.update(pin, id, updated);
    }

    @RequestMapping(value = "/{pin}/orders/bar/{id}/status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderStatusPUT getStatus(@PathVariable String pin, @PathVariable Long id) {
        return service.getStatus(pin, id);
    }

    @RequestMapping(value = "/{pin}/orders/bar/{id}/status", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderStatusPUT updateStatus(@PathVariable String pin, @PathVariable Long id, @Valid @RequestBody OrderStatusPUT newStatus) {
        return service.updateStatus(pin, id, newStatus);
    }
}
