package com.horeca.site.services.services;

import com.horeca.site.models.hotel.services.AvailableServices;
import com.horeca.site.models.hotel.services.spacall.SpaCall;
import com.horeca.site.repositories.services.SpaCallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SpaCallService extends GenericHotelService<SpaCall> {

    private AvailableServicesService availableServicesService;

    @Autowired
    public SpaCallService(SpaCallRepository repository,
                          AvailableServicesService availableServicesService) {
        super(repository);
        this.availableServicesService = availableServicesService;
    }

    public SpaCall get(Long hotelId) {
        AvailableServices services = availableServicesService.get(hotelId);
        return services.getSpaCall();
    }
}
