package com.horeca.site.services;

import com.horeca.site.models.cubilis.CubilisSettings;
import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.repositories.CubilisSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CubilisSettingsService {

    @Autowired
    private CubilisSettingsRepository repository;

    @Autowired
    private HotelService hotelService;

    public CubilisSettings get(Long hotelId) {
        Hotel hotel = hotelService.get(hotelId);
        return hotel.getCubilisSettings();
    }

    public CubilisSettings update(Long hotelId, CubilisSettings updated) {
        CubilisSettings current = get(hotelId);
        updated.setId(current.getId());
        return repository.save(updated);
    }
}
