package com.horeca.site.services.services;

import com.horeca.site.models.hotel.services.AvailableServices;
import com.horeca.site.models.hotel.services.taxi.Taxi;
import com.horeca.site.models.hotel.services.taxi.TaxiItem;
import com.horeca.site.repositories.services.TaxiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class TaxiService {

    @Autowired
    private AvailableServicesService availableServicesService;

    @Autowired
    private TaxiRepository repository;

    public Taxi get(Long hotelId) {
        AvailableServices services = availableServicesService.get(hotelId);
        return services.getTaxi();
    }

    public Taxi addItem(Long hotelId, TaxiItem item) {
        Taxi taxi = get(hotelId);
        taxi.getItems().add(item);
        return repository.save(taxi);
    }

    public Taxi updateItem(Long hotelId, TaxiItem item) {
        Taxi taxi = get(hotelId);
        // TODO improve the solution; account for the possibility that there's no such item
        for (TaxiItem taxiItem : taxi.getItems()) {
            if (taxiItem.getId().equals(item.getId())) {
                taxiItem.setName(item.getName());
                taxiItem.setNumber(item.getNumber());
            }
        }
        return repository.save(taxi);
    }

    public Iterable<TaxiItem> getItems(Long hotelId) {
        Taxi taxi = get(hotelId);
        return taxi.getItems();
    }

    public void deleteItem(Long hotelId, Long itemId) {
        Taxi taxi = get(hotelId);
        Set<TaxiItem> remaining = new HashSet<>();
        for (TaxiItem taxiItem : taxi.getItems()) {
            if (!taxiItem.getId().equals(itemId))
                remaining.add(taxiItem);
        }

        taxi.setItems(remaining);
        repository.save(taxi);
    }
}
