package com.horeca.site.controllers.orders;

import com.horeca.annotations.AllowCORS;
import com.horeca.site.models.orders.OrderStatusPUT;
import com.horeca.site.models.orders.petcare.PetCareOrder;
import com.horeca.site.models.orders.petcare.PetCareOrderPOST;
import com.horeca.site.models.orders.petcare.PetCareOrderView;
import com.horeca.site.services.orders.PetCareOrderService;
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
public class PetCareOrderController {

    @Autowired
    private PetCareOrderService service;

    @RequestMapping(value = "/{pin}/orders/petcare", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<PetCareOrderView> getAll(@PathVariable String pin, HttpServletRequest request) {
        String preferredLanguage = request.getLocale().getLanguage();
        return service.getAllViews(pin, preferredLanguage);
    }

    @RequestMapping(value = "/{pin}/orders/petcare/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public PetCareOrderView get(@PathVariable String pin, @PathVariable Long id, HttpServletRequest request) {
        String preferredLanguage = request.getLocale().getLanguage();
        return service.getView(pin, id, preferredLanguage);
    }

    @RequestMapping(value = "/{pin}/orders/petcare", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public PetCareOrder add(@PathVariable String pin, @Valid @RequestBody PetCareOrderPOST newOrder) {
        return service.add(pin, newOrder);
    }

    @RequestMapping(value = "/{pin}/orders/petcare/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public PetCareOrder update(@PathVariable String pin, @PathVariable Long id, @Valid @RequestBody PetCareOrder updated) {
        return service.update(pin, id, updated);
    }

    @RequestMapping(value = "/{pin}/orders/petcare/{id}/status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderStatusPUT getStatus(@PathVariable String pin, @PathVariable Long id) {
        return service.getStatus(pin, id);
    }

    @RequestMapping(value = "/{pin}/orders/petcare/{id}/status", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderStatusPUT updateStatus(@PathVariable String pin, @PathVariable Long id, @Valid @RequestBody OrderStatusPUT newStatus) {
        return service.updateStatus(pin, id, newStatus);
    }
}
