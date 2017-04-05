package com.horeca.site.services.services;

import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.hotel.services.AvailableServices;
import com.horeca.site.repositories.services.AvailableServicesRepository;
import com.horeca.site.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AvailableServicesService {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private AvailableServicesRepository repository;

    public AvailableServices get(Long hotelId) {
        Hotel hotel = hotelService.get(hotelId);
        return hotel.getAvailableServices();
    }

    public AvailableServices update(AvailableServices services) {
        return repository.save(services);
    }

    public AvailableServices addIfDoesntExistAndGet(Long hotelId) {
        AvailableServices services = get(hotelId);
        if (services == null) {
            services = new AvailableServices();
            repository.save(services);
            Hotel hotel = hotelService.get(hotelId);
            hotel.setAvailableServices(services);
            hotelService.update(hotel.getId(), hotel);
        }
        return services;
    }
}
