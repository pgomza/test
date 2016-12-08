package com.horeca.site.services.services;

import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.hotel.services.carpark.CarPark;
import com.horeca.site.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CarParkService {

    @Autowired
    private HotelService hotelService;

    public CarParkView getView(Long hotelId, String preferredLanguage) {
        Hotel hotel = hotelService.get(hotelId);
        CarPark carPark = hotel.getAvailableServices().getCarPark();
        return carPark.toView(preferredLanguage, hotel.getDefaultTranslation());
    }
}
