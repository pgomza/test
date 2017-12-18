package com.horeca.site.services.services;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.Currency;
import com.horeca.site.models.Price;
import com.horeca.site.models.hotel.services.AvailableServices;
import com.horeca.site.models.hotel.services.taxi.Taxi;
import com.horeca.site.models.hotel.services.taxi.TaxiItem;
import com.horeca.site.repositories.services.TaxiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
        if (services == null || services.getTaxi() == null)
            throw new ResourceNotFoundException();
        return services.getTaxi();
    }

    public Taxi addDefaultTaxi(Long hotelId) {
        AvailableServices services = availableServicesService.addIfDoesntExistAndGet(hotelId);
        if (services.getTaxi() == null) {
            Taxi taxi = new Taxi();
            Price taxiPrice = new Price();
            taxiPrice.setCurrency(Currency.EUR);
            taxiPrice.setValue(new BigDecimal(5));
            taxi.setPrice(taxiPrice);

            services.setTaxi(taxi);
            AvailableServices updatedServices = availableServicesService.update(services);
            return updatedServices.getTaxi();
        }
        else {
            throw new BusinessRuleViolationException("A taxi service has already been added");
        }
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
