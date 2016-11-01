package com.horeca.site.controllers.orders;

import com.horeca.annotations.AllowCORS;
import com.horeca.site.models.orders.dnd.DndOrder;
import com.horeca.site.models.orders.dnd.DndOrderUPDATE;
import com.horeca.site.services.DndOrderService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "orders")
@AllowCORS
@RestController
@RequestMapping("/api/stays")
public class DndOrderController {

    @Autowired
    private DndOrderService service;

    @RequestMapping(value = "/{pin}/orders/dnd", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DndOrder get(@PathVariable String pin) {
        return service.get(pin);
    }

    @RequestMapping(value = "/{pin}/orders/dnd", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public DndOrder update(@PathVariable String pin, @Valid @RequestBody DndOrderUPDATE updated) {
        return service.update(pin, updated);
    }
}
