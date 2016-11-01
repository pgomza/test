package com.horeca.site.services.services;

import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.hotel.services.breakfast.Breakfast;
import com.horeca.site.models.hotel.services.breakfast.BreakfastView;
import com.horeca.site.repositories.services.BreakfastRepository;
import com.horeca.site.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BreakfastService {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private BreakfastRepository repository;

    public Breakfast get(Long hotelId) {
        Hotel hotel = hotelService.get(hotelId);
        return hotel.getAvailableServices().getBreakfast();
    }

    public BreakfastView getView(Long hotelId, String preferredLanguage) {
        Breakfast breakfast = get(hotelId);
        return breakfast.toView(preferredLanguage, hotelService.get(hotelId).getDefaultTranslation());
    }
}
