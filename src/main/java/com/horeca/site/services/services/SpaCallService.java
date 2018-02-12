package com.horeca.site.services.services;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.Price;
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
        if (services == null || services.getSpaCall() == null)
            throw new ResourceNotFoundException();
        return services.getSpaCall();
    }

    public SpaCall addDefaultSpaCall(Long hotelId) {
        AvailableServices services = availableServicesService.get(hotelId);
        if (services.getSpaCall() == null) {
            SpaCall spaCall = new SpaCall();
            spaCall.setDescription("");
            spaCall.setNumber("+12 345 678 901");
            Price price = new Price();
            price.setText("Free");

            services.setSpaCall(spaCall);
            AvailableServices updatedServices = availableServicesService.update(services);
            return updatedServices.getSpaCall();
        }
        else {
            throw new BusinessRuleViolationException("A spa call service has already been added");
        }
    }
}
