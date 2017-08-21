package com.horeca.site.services.services;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.Currency;
import com.horeca.site.models.Price;
import com.horeca.site.models.hotel.services.AvailableServices;
import com.horeca.site.models.hotel.services.housekeeping.Housekeeping;
import com.horeca.site.models.hotel.services.housekeeping.HousekeepingItem;
import com.horeca.site.repositories.services.HousekeepingItemRepository;
import com.horeca.site.repositories.services.HousekeepingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
public class HousekeepingService {

    @Autowired
    private AvailableServicesService availableServicesService;

    @Autowired
    private HousekeepingRepository repository;

    @Autowired
    private HousekeepingItemRepository itemRepository;

    public Housekeeping get(Long hotelId) {
        AvailableServices services = availableServicesService.get(hotelId);
        if (services == null || services.getHousekeeping() == null)
            throw new ResourceNotFoundException();
        return services.getHousekeeping();
    }

    public Housekeeping addDefaultHousekeeping(Long hotelId) {
        AvailableServices services = availableServicesService.addIfDoesntExistAndGet(hotelId);
        if (services.getHousekeeping() == null) {
            Housekeeping housekeeping = new Housekeeping();
            housekeeping.setDescription("");
            Price housekeepingPrice = new Price();
            housekeepingPrice.setCurrency(Currency.EURO);
            housekeepingPrice.setValue(new BigDecimal(5));
            housekeeping.setPrice(housekeepingPrice);

            services.setHousekeeping(housekeeping);
            AvailableServices updatedServices = availableServicesService.update(services);
            return updatedServices.getHousekeeping();
        }
        else {
            throw new BusinessRuleViolationException("A housekeeping service has already been added");
        }
    }

    public Iterable<HousekeepingItem> getItems(Long hotelId) {
        Housekeeping housekeeping = get(hotelId);
        return housekeeping.getItems();
    }

    public HousekeepingItem getItem(Long hotelId, Long itemId) {
        Housekeeping housekeeping = get(hotelId);

        Optional<HousekeepingItem> matchingItemOptional = housekeeping.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findAny();

        if (!matchingItemOptional.isPresent())
            throw new ResourceNotFoundException("Could not find an item with such an id");

        return matchingItemOptional.get();
    }

    public HousekeepingItem addItem(Long hotelId, HousekeepingItem item) {
        Housekeeping housekeeping = get(hotelId);
        HousekeepingItem savedItem = itemRepository.save(item);
        housekeeping.getItems().add(savedItem);
        repository.save(housekeeping);
        return savedItem;
    }

    public HousekeepingItem updateItem(Long hotelId, HousekeepingItem updatedItem) {
        getItem(hotelId, updatedItem.getId());
        return itemRepository.save(updatedItem);
    }

    public void deleteItem(Long hotelId, Long itemId) {
        HousekeepingItem item = getItem(hotelId, itemId);
        Housekeeping housekeeping = get(hotelId);

        housekeeping.getItems().remove(item);

        repository.save(housekeeping);
    }
}
