package com.horeca.site.services;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.hotel.HotelTranslation;
import com.horeca.site.models.hotel.HotelView;
import com.horeca.site.models.hotel.address.AddressView;
import com.horeca.site.models.hotel.roomdirectory.RoomDirectoryView;
import com.horeca.site.repositories.HotelRepository;
import com.horeca.site.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.SUPPORTS)
public class HotelService {

    @Autowired
    private HotelRepository repository;

    public Iterable<Hotel> getAll() {
        return repository.findAll();
    }

    public Hotel get(Long id) {
        Hotel hotel = repository.findOne(id);
        if (hotel == null)
            throw new ResourceNotFoundException();

        return hotel;
    }

    public Hotel add(Hotel hotel) {
        String defaultTranslation = hotel.getDefaultTranslation();
        Set<HotelTranslation> translations = hotel.getTranslations();

        boolean exists = false;
        for (HotelTranslation translation : translations) {
            if (translation.getLanguage().equals(defaultTranslation)) {
                exists = true;
                break;
            }
        }
        if (!exists)
            throw new BusinessRuleViolationException("The default language has been specified but the corresponding translation has not been provided");

        return repository.save(hotel);
    }

    public Hotel update(Long id, Hotel newOne) {
        Hotel oldOne = get(id);
        newOne.setId(id);
        Hotel changed = repository.save(newOne);

        return changed;
    }

    public void delete(Long id) {
        Hotel toDelete = get(id);
        repository.delete(toDelete);
    }

    public List<HotelView> getAllViews(Iterable<Hotel> hotels, String preferredLanguage) {
        List<HotelView> hotelViews = new ArrayList<>();

        for (Hotel hotel : hotels) {
            HotelView hotelView = hotel.toView(preferredLanguage, hotel.getDefaultTranslation());
            hotelViews.add(hotelView);
        }

        return hotelViews;
    }
}
