package com.horeca.site.services.services;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.hotel.services.AvailableServices;
import com.horeca.site.models.hotel.services.tableordering.TableOrdering;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class TableOrderingService {

    @Autowired
    private AvailableServicesService availableServicesService;

    public TableOrdering get(Long hotelId) {
        AvailableServices services = availableServicesService.get(hotelId);
        if (services == null || services.getTableOrdering() == null)
            throw new ResourceNotFoundException();
        return services.getTableOrdering();
    }

    public TableOrdering addDefaultTableOrdering(Long hotelId) {
        AvailableServices services = availableServicesService.addIfDoesntExistAndGet(hotelId);
        if (services.getTableOrdering() == null) {
            TableOrdering tableOrdering = new TableOrdering("");
            services.setTableOrdering(tableOrdering);
            AvailableServices updatedServices = availableServicesService.update(services);
            return updatedServices.getTableOrdering();
        }
        else {
            throw new BusinessRuleViolationException("A table ordering service has already been added");
        }
    }
}
