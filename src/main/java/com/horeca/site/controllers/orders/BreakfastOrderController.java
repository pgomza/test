package com.horeca.site.controllers.orders;

import com.horeca.annotations.AllowCORS;
import com.horeca.site.models.orders.OrderStatusPUT;
import com.horeca.site.models.orders.breakfast.BreakfastOrder;
import com.horeca.site.models.orders.breakfast.BreakfastOrderPOST;
import com.horeca.site.services.orders.BreakfastOrderService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Set;

@Api(value = "orders")
@AllowCORS
@RestController
@RequestMapping("/api/stays")
public class BreakfastOrderController {

    @Autowired
    private BreakfastOrderService service;

    @RequestMapping(value = "/{pin}/orders/breakfast", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<BreakfastOrderView> getAll(@PathVariable String pin, HttpServletRequest request) {
        String preferredLanguage = request.getLocale().getLanguage();
        return service.getAllViews(pin, preferredLanguage);
    }

    @RequestMapping(value = "/{pin}/orders/breakfast/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public BreakfastOrderView get(@PathVariable String pin, @PathVariable Long id, HttpServletRequest request) {
        String preferredLanguage = request.getLocale().getLanguage();
        return service.getView(pin, id, preferredLanguage);
    }

    @RequestMapping(value = "/{pin}/orders/breakfast", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public BreakfastOrder add(@PathVariable String pin, @Valid @RequestBody BreakfastOrderPOST newOrder) {
        return service.add(pin, newOrder);
    }

    @RequestMapping(value = "/{pin}/orders/breakfast/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public BreakfastOrder update(@PathVariable String pin, @PathVariable Long id, @Valid @RequestBody BreakfastOrder updated) {
        return service.update(pin, id, updated);
    }

    @RequestMapping(value = "/{pin}/orders/breakfast/{id}/status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderStatusPUT getStatus(@PathVariable String pin, @PathVariable Long id) {
        return service.getStatus(pin, id);
    }

    @RequestMapping(value = "/{pin}/orders/breakfast/{id}/status", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderStatusPUT updateStatus(@PathVariable String pin, @PathVariable Long id, @Valid @RequestBody OrderStatusPUT newStatus) {
        return service.updateStatus(pin, id, newStatus);
    }
}
