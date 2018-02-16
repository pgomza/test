package com.horeca.site.services.services;

import com.horeca.site.models.hotel.services.AvailableServices;
import com.horeca.site.models.hotel.services.carpark.CarPark;
import com.horeca.site.repositories.services.CarParkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CarParkService extends GenericHotelService<CarPark> {

    private AvailableServicesService availableServicesService;

    @Autowired
    public CarParkService(CarParkRepository repository,
                          AvailableServicesService availableServicesService) {
        super(repository);
        this.availableServicesService = availableServicesService;
    }

    public CarPark get(Long hotelId) {
        AvailableServices services = availableServicesService.get(hotelId);
        return services.getCarPark();
    }
}
