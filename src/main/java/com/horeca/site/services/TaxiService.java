package com.horeca.site.services;

import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.hotel.services.taxi.Taxi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TaxiService {

    @Autowired
    private HotelService hotelService;

    public Taxi get(Long hotelId) {
        Hotel hotel = hotelService.get(hotelId);
        return hotel.getAvailableServices().getTaxi();
    }
}
