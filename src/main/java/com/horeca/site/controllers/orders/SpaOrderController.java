package com.horeca.site.controllers.orders;

import com.horeca.site.models.orders.OrderStatusPUT;
import com.horeca.site.models.orders.spa.SpaOrder;
import com.horeca.site.models.orders.spa.SpaOrderPOST;
import com.horeca.site.security.models.GuestAccount;
import com.horeca.site.services.orders.SpaOrderService;
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
public class SpaOrderController {

    @Autowired
    private SpaOrderService service;

    @RequestMapping(value = "/{pin}/orders/spa", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<SpaOrder> getAll(@PathVariable String pin) {
        return service.getAll(pin);
    }

    @RequestMapping(value = "/{pin}/orders/spa/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public SpaOrder get(@PathVariable String pin, @PathVariable Long id) {
        return service.get(pin, id);
    }

    @RequestMapping(value = "/{pin}/orders/spa", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public SpaOrder add(@PathVariable String pin, @Valid @RequestBody SpaOrderPOST newOrder, Authentication authentication) {
        if (authentication.getPrincipal() instanceof GuestAccount) {
            return service.addAndTryToNotify(pin, newOrder);
        }
        else
            return service.add(pin, newOrder);
    }

    @RequestMapping(value = "/{pin}/orders/spa/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public SpaOrder update(@PathVariable String pin, @PathVariable Long id, @Valid @RequestBody SpaOrder updated) {
        return service.update(pin, id, updated);
    }

    @RequestMapping(value = "/{pin}/orders/spa/{id}/status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderStatusPUT getStatus(@PathVariable String pin, @PathVariable Long id) {
        return service.getStatus(pin, id);
    }

    @RequestMapping(value = "/{pin}/orders/spa/{id}/status", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderStatusPUT updateStatus(@PathVariable String pin, @PathVariable Long id, @Valid @RequestBody OrderStatusPUT newStatus) {
        return service.updateStatus(pin, id, newStatus);
    }
}
