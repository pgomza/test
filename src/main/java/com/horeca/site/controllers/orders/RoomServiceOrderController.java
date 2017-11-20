package com.horeca.site.controllers.orders;

import com.horeca.site.handlers.StayPin;
import com.horeca.site.handlers.TranslateReturnValue;
import com.horeca.site.models.orders.OrderStatusPUT;
import com.horeca.site.models.orders.roomservice.RoomServiceOrder;
import com.horeca.site.models.orders.roomservice.RoomServiceOrderPOST;
import com.horeca.site.security.models.GuestAccount;
import com.horeca.site.services.orders.RoomServiceOrderService;
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
public class RoomServiceOrderController {

    @Autowired
    private RoomServiceOrderService service;

    @TranslateReturnValue
    @RequestMapping(value = "/{pin}/orders/roomservice", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<RoomServiceOrder> getAll(@StayPin @PathVariable String pin) {
        return service.getAll(pin);
    }

    @TranslateReturnValue
    @RequestMapping(value = "/{pin}/orders/roomservice/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public RoomServiceOrder get(@StayPin @PathVariable String pin, @PathVariable Long id) {
        return service.get(pin, id);
    }

    @RequestMapping(value = "/{pin}/orders/roomservice", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public RoomServiceOrder add(@PathVariable String pin, @Valid @RequestBody RoomServiceOrderPOST newOrder,
                                Authentication authentication) {
        if (authentication.getPrincipal() instanceof GuestAccount) {
            return service.addAndNotify(pin, newOrder);
        }
        else
            throw new AccessDeniedException("You are not allowed to add a new order");
    }

    @RequestMapping(value = "/{pin}/orders/roomservice/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public RoomServiceOrder update(@PathVariable String pin, @PathVariable Long id, @Valid @RequestBody RoomServiceOrder updated) {
        return service.update(pin, id, updated);
    }

    @RequestMapping(value = "/{pin}/orders/roomservice/{id}/status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderStatusPUT getStatus(@PathVariable String pin, @PathVariable Long id) {
        return service.getStatus(pin, id);
    }

    @RequestMapping(value = "/{pin}/orders/roomservice/{id}/status", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderStatusPUT updateStatus(@PathVariable String pin, @PathVariable Long id, @Valid @RequestBody OrderStatusPUT newStatus) {
        return service.updateStatus(pin, id, newStatus);
    }
}
