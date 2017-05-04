package com.horeca.site.controllers.orders;

import com.horeca.site.models.orders.OrderStatusPUT;
import com.horeca.site.models.orders.taxi.TaxiOrder;
import com.horeca.site.models.orders.taxi.TaxiOrderPOST;
import com.horeca.site.security.models.GuestAccount;
import com.horeca.site.services.orders.TaxiOrderService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@Api(value = "orders")
@RestController
@RequestMapping("/api/stays")
public class TaxiOrderController {

    @Autowired
    private TaxiOrderService service;

    @RequestMapping(value = "/{pin}/orders/taxi", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<TaxiOrder> getAll(@PathVariable String pin) {
        return service.getAll(pin);
    }

    @RequestMapping(value = "/{pin}/orders/taxi/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public TaxiOrder get(@PathVariable String pin, @PathVariable Long id) {
        return service.get(pin, id);
    }

    @RequestMapping(value = "/{pin}/orders/taxi", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public TaxiOrder add(@PathVariable String pin, @Valid @RequestBody TaxiOrderPOST newOrder) {
        Object authentication = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (authentication instanceof GuestAccount) {
            return service.addAndTryToNotify(pin, newOrder);
        }
        else
            return service.add(pin, newOrder);
    }

    @RequestMapping(value = "/{pin}/orders/taxi/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public TaxiOrder update(@PathVariable String pin, @PathVariable Long id, @Valid @RequestBody TaxiOrder updated) {
        return service.update(pin, id, updated);
    }

    @RequestMapping(value = "/{pin}/orders/taxi/{id}/status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderStatusPUT getStatus(@PathVariable String pin, @PathVariable Long id) {
        return service.getStatus(pin, id);
    }

    @RequestMapping(value = "/{pin}/orders/taxi/{id}/status", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderStatusPUT updateStatus(@PathVariable String pin, @PathVariable Long id, @Valid @RequestBody OrderStatusPUT newStatus) {
        return service.updateStatus(pin, id, newStatus);
    }
}
