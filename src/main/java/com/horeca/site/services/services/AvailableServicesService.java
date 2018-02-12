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
}
