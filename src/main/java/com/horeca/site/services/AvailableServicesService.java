package com.horeca.site.services;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.hotel.services.AvailableServices;
import com.horeca.site.models.hotel.services.AvailableServicesView;
import com.horeca.site.models.hotel.services.breakfast.Breakfast;
import com.horeca.site.models.hotel.services.breakfast.BreakfastView;
import com.horeca.site.repositories.AvailableServicesRepository;
import com.horeca.site.repositories.BreakfastRepository;
import com.horeca.site.repositories.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AvailableServicesService {

    @Autowired
    private BreakfastRepository breakfastRepository;

    public AvailableServices get(Hotel hotel) {
        AvailableServices services = hotel.getAvailableServices();
        if (services == null)
            throw new ResourceNotFoundException();

        return services;
    }

    public Breakfast getBreakfast(Hotel hotel) {
        AvailableServices services = get(hotel);
        Breakfast breakfast = services.getBreakfast();
        if (breakfast == null)
            throw new ResourceNotFoundException();

        return breakfast;
    }

    public Breakfast updateBreakfast(Long breakfastId, Breakfast newOne) {
        Breakfast oldOne = breakfastRepository.findOne(breakfastId);
        oldOne.setPrice(newOne.getPrice());
        oldOne.setTranslations(newOne.getTranslations());
        return breakfastRepository.save(oldOne);
    }
}
