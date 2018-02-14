package com.horeca.site.services.services;

import com.horeca.site.models.hotel.services.AvailableServices;
import com.horeca.site.models.hotel.services.tableordering.TableOrdering;
import com.horeca.site.repositories.services.TableOrderingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class TableOrderingService extends GenericHotelService<TableOrdering> {

    private AvailableServicesService availableServicesService;

    @Autowired
    public TableOrderingService(TableOrderingRepository repository,
                                AvailableServicesService availableServicesService) {
        super(repository);
        this.availableServicesService = availableServicesService;
    }

    public TableOrdering get(Long hotelId) {
        AvailableServices services = availableServicesService.get(hotelId);
        return services.getTableOrdering();
    }
}
