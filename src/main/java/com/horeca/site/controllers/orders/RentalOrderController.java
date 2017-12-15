package com.horeca.site.controllers.orders;

import com.horeca.site.handlers.ReplaceCurrency;
import com.horeca.site.handlers.StayPin;
import com.horeca.site.handlers.TranslateReturnValue;
import com.horeca.site.models.orders.OrderStatusPUT;
import com.horeca.site.models.orders.rental.RentalOrder;
import com.horeca.site.models.orders.rental.RentalOrderPOST;
import com.horeca.site.security.models.GuestAccount;
import com.horeca.site.services.orders.RentalOrderService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@Api(value = "orders")
@RestController
@RequestMapping("/api/stays")
public class RentalOrderController {

    @Autowired
    private RentalOrderService service;

    @ReplaceCurrency
    @TranslateReturnValue
    @RequestMapping(value = "/{pin}/orders/rental", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<RentalOrder> getAll(@StayPin @PathVariable String pin) {
        return service.getAll(pin);
    }

    @ReplaceCurrency
    @TranslateReturnValue
    @RequestMapping(value = "/{pin}/orders/rental/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public RentalOrder get(@StayPin @PathVariable String pin, @PathVariable Long id) {
        return service.get(pin, id);
    }

    @RequestMapping(value = "/{pin}/orders/rental", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public RentalOrder add(@PathVariable String pin, @Valid @RequestBody RentalOrderPOST newOrder,
                        Authentication authentication) {
        if (authentication.getPrincipal() instanceof GuestAccount) {
            return service.addAndNotify(pin, newOrder);
        }
        else
            throw new AccessDeniedException("You are not allowed to add a new order");
    }

    @RequestMapping(value = "/{pin}/orders/rental/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public RentalOrder update(@PathVariable String pin, @PathVariable Long id, @Valid @RequestBody RentalOrder updated) {
        return service.update(pin, id, updated);
    }

    @RequestMapping(value = "/{pin}/orders/rental/{id}/status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderStatusPUT getStatus(@PathVariable String pin, @PathVariable Long id) {
        return service.getStatus(pin, id);
    }

    @RequestMapping(value = "/{pin}/orders/rental/{id}/status", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderStatusPUT updateStatus(@PathVariable String pin, @PathVariable Long id, @Valid @RequestBody OrderStatusPUT newStatus) {
        return service.updateStatus(pin, id, newStatus);
    }
}