package com.horeca.site.services.services;

import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.hotel.services.AvailableServices;
import com.horeca.site.models.hotel.services.AvailableServicesView;
import com.horeca.site.repositories.services.BreakfastRepository;
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
    private BreakfastRepository repository;

    public AvailableServices get(Long hotelId) {
        Hotel hotel = hotelService.get(hotelId);
        return hotel.getAvailableServices();
    }

    public AvailableServicesView getView(Long hotelId, String preferredLanguage) {
        AvailableServices availableServices = get(hotelId);
        return availableServices.toView(preferredLanguage, hotelService.get(hotelId).getDefaultTranslation());
    }
}
