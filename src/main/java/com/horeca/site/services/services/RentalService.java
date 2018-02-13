package com.horeca.site.services.services;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.hotel.services.AvailableServices;
import com.horeca.site.models.hotel.services.rental.Rental;
import com.horeca.site.models.hotel.services.rental.RentalCategory;
import com.horeca.site.models.hotel.services.rental.RentalItem;
import com.horeca.site.models.hotel.services.rental.RentalItemUpdate;
import com.horeca.site.repositories.services.RentalCategoryRepository;
import com.horeca.site.repositories.services.RentalItemRepository;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class RentalService {

    private static final DateTimeFormatter localTimeFormatter = DateTimeFormat.forPattern("HH:mm");

    @Autowired
    private AvailableServicesService availableServicesService;

    @Autowired
    private RentalCategoryRepository rentalCategoryRepository;

    @Autowired
    private RentalItemRepository rentalItemRepository;

    public Rental get(Long hotelId) {
        AvailableServices services = availableServicesService.get(hotelId);
        return services.getRental();
    }

    public RentalCategory getCategory(Long hotelId, RentalCategory.Category categoryName) {
        return get(hotelId).getCategories().stream()
                .filter(category -> category.getCategory() == categoryName)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("There is no such category"));
    }

    public void addItem(Long hotelId, RentalItemUpdate item) {
        RentalCategory category = getCategory(hotelId, item.getType());
        if (category != null) {
            RentalItem itemNew = new RentalItem();
            itemNew.setPrice(item.getPrice());
            itemNew.setAvailable(item.isAvailable());
            itemNew.setName(item.getName());
            category.getItems().add(itemNew);
            rentalCategoryRepository.save(category);
        }
        else
            throw new ResourceNotFoundException("There is no such category");
    }

    public RentalItem updateItem(Long hotelId, RentalItemUpdate itemSent) {
        Rental rental = get(hotelId);
        Set<RentalCategory> categories = rental.getCategories();

        RentalItem found = null;
        RentalCategory inCategory = null;
        for (RentalCategory rentalCategory : categories) {
            Optional<RentalItem> filteredItem = rentalCategory.getItems().stream()
                    .filter(item -> item.getId().equals(itemSent.getId()))
                    .findFirst();

            if (filteredItem.isPresent()) {
                found = filteredItem.get();
                inCategory = rentalCategory;
                break;
            }
        }

        if (found == null)
            throw new ResourceNotFoundException("Could not find an item with such an id");

        found.setPrice(itemSent.getPrice());
        found.setAvailable(itemSent.isAvailable());
        found.setName(itemSent.getName());
        RentalItem savedItem = rentalItemRepository.save(found);

        if (itemSent.getType() != inCategory.getCategory()) {
            RentalCategory itemSentCategory = getCategory(hotelId, itemSent.getType());
            itemSentCategory.getItems().add(savedItem);
            rentalCategoryRepository.save(itemSentCategory);
        }

        return savedItem;
    }

    public void deleteItem(Long hotelId, Long idToDelete) {
        Rental rental = get(hotelId);
        Set<RentalCategory> categories = rental.getCategories();

        boolean found = false;
        for (RentalCategory rentalCategory : categories) {
            Optional<RentalItem> filteredItem = rentalCategory.getItems().stream()
                    .filter(item -> item.getId().equals(idToDelete))
                    .findFirst();

            if (filteredItem.isPresent()) {
                rentalItemRepository.delete(filteredItem.get());
                found = true;
                break;
            }
        }

        if (!found)
            throw new ResourceNotFoundException("Could not find an item with such an id");
    }
}
