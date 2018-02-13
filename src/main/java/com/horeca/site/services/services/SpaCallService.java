package com.horeca.site.services.services;

import com.horeca.site.models.hotel.services.AvailableServices;
import com.horeca.site.models.hotel.services.spacall.SpaCall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SpaCallService {

    @Autowired
    private AvailableServicesService availableServicesService;

    public SpaCall get(Long hotelId) {
        AvailableServices services = availableServicesService.get(hotelId);
        return services.getSpaCall();
    }
}
