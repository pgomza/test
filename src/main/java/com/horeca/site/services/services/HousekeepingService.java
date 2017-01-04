package com.horeca.site.services.services;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.hotel.services.housekeeping.Housekeeping;
import com.horeca.site.models.hotel.services.housekeeping.HousekeepingItem;
import com.horeca.site.repositories.services.HousekeepingRepository;
import com.horeca.site.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class HousekeepingService {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private HousekeepingRepository repository;

    public Housekeeping get(Long hotelId) {
        Hotel hotel = hotelService.get(hotelId);
        return hotel.getAvailableServices().getHousekeeping();
    }

    public Iterable<HousekeepingItem> getItems(Long hotelId) {
        Housekeeping housekeeping = get(hotelId);
        return housekeeping.getItems();
    }

    public Housekeeping addItem(Long hotelId, HousekeepingItem item) {
        Housekeeping housekeeping = get(hotelId);
        housekeeping.getItems().add(item);
        return repository.save(housekeeping);
    }

    public Housekeeping updateItem(Long hotelId, HousekeepingItem item) {
        Housekeeping housekeeping = get(hotelId);

        boolean found = false;
        for (HousekeepingItem existingItem : housekeeping.getItems()) {
            if (existingItem.getId().equals(item.getId())) {
                existingItem.setName(item.getName());
                found = true;
                break;
            }
        }

        if (!found)
            throw new ResourceNotFoundException("Could not find an item with such an id");

        return repository.save(housekeeping);
    }

    public void deleteItem(Long hotelId, Long itemId) {
        Housekeeping housekeeping = get(hotelId);
        Set<HousekeepingItem> remaining = new HashSet<>();

        boolean found = false;
        for (HousekeepingItem existingItem : housekeeping.getItems()) {
            if (!existingItem.getId().equals(itemId))
                remaining.add(existingItem);
            else
                found = true;
        }

        if (!found)
            throw new ResourceNotFoundException("Could not find an item with such an id");

        housekeeping.setItems(remaining);
        repository.save(housekeeping);
    }
}
