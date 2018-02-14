package com.horeca.site.services.services;

import com.horeca.site.models.hotel.services.AvailableServices;
import com.horeca.site.models.hotel.services.hairdresser.HairDresser;
import com.horeca.site.repositories.services.HairDresserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class HairDresserService extends GenericHotelService<HairDresser> {

    private AvailableServicesService availableServicesService;

    @Autowired
    public HairDresserService(HairDresserRepository repository,
                              AvailableServicesService availableServicesService) {
        super(repository);
        this.availableServicesService = availableServicesService;
    }

    public HairDresser get(Long hotelId) {
        AvailableServices services = availableServicesService.get(hotelId);
        return services.getHairDresser();
    }
}
