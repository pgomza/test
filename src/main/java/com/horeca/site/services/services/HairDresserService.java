package com.horeca.site.services.services;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.Price;
import com.horeca.site.models.hotel.services.AvailableServices;
import com.horeca.site.models.hotel.services.hairdresser.HairDresser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class HairDresserService {

    @Autowired
    private AvailableServicesService availableServicesService;

    public HairDresser get(Long hotelId) {
        AvailableServices services = availableServicesService.get(hotelId);
        if (services == null || services.getHairDresser() == null)
            throw new ResourceNotFoundException();
        return services.getHairDresser();
    }

    public HairDresser addDefaultHairDresser(Long hotelId) {
        AvailableServices services = availableServicesService.addIfDoesntExistAndGet(hotelId);
        if (services.getHairDresser() == null) {
            HairDresser hairDresser = new HairDresser();
            hairDresser.setDescription("");
            hairDresser.setNumber("+12 345 678 901");
            Price price = new Price();
            price.setText("Free");

            services.setHairDresser(hairDresser);
            AvailableServices updatedServices = availableServicesService.update(services);
            return updatedServices.getHairDresser();
        }
        else {
            throw new BusinessRuleViolationException("A hair dresser service has already been added");
        }
    }
}
