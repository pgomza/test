package com.horeca.site.services;

import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.notifications.NotificationSettings;
import com.horeca.site.repositories.NotificationSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NotificationSettingsService {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private NotificationSettingsRepository repository;

    public NotificationSettings get(Long hotelId) {
        Hotel hotel = hotelService.get(hotelId);
        return hotel.getNotificationSettings();
    }

    public NotificationSettings update(Long hotelId, NotificationSettings updated) {
        Hotel hotel = hotelService.get(hotelId);
        Long existingId = hotel.getNotificationSettings().getId();
        updated.setId(existingId);
        return repository.save(updated);
    }
}
