package com.horeca.site.services.services;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.Currency;
import com.horeca.site.models.Price;
import com.horeca.site.models.hotel.services.AvailableServices;
import com.horeca.site.models.hotel.services.carpark.CarPark;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class CarParkService {

    @Autowired
    private AvailableServicesService availableServicesService;

    public CarPark get(Long hotelId) {
        AvailableServices services = availableServicesService.get(hotelId);
        if (services == null || services.getCarPark() == null)
            throw new ResourceNotFoundException();
        return services.getCarPark();
    }

    public CarPark addDefaultCarPark(Long hotelId) {
        AvailableServices services = availableServicesService.get(hotelId);
        if (services.getCarPark() == null) {
            CarPark carPark = new CarPark();
            carPark.setDescription("");
            Price carParkPrice = new Price();
            carParkPrice.setCurrency(Currency.EUR);
            carParkPrice.setValue(new BigDecimal(5));
            carPark.setPrice(carParkPrice);

            services.setCarPark(carPark);
            AvailableServices updatedServices = availableServicesService.update(services);
            return updatedServices.getCarPark();
        }
        else {
            throw new BusinessRuleViolationException("A carpark service has already been added");
        }
    }
}
