package com.horeca.site.services.services;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.Currency;
import com.horeca.site.models.Price;
import com.horeca.site.models.hotel.services.AvailableServices;
import com.horeca.site.models.hotel.services.bar.Bar;
import com.horeca.site.models.hotel.services.bar.BarCategory;
import com.horeca.site.models.hotel.services.bar.BarItem;
import com.horeca.site.models.hotel.services.bar.BarItemUpdate;
import com.horeca.site.repositories.services.BarCategoryRepository;
import com.horeca.site.repositories.services.BarItemRepository;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class BarService {

    private static final DateTimeFormatter localTimeFormatter = DateTimeFormat.forPattern("HH:mm");

    @Autowired
    private AvailableServicesService availableServicesService;

    @Autowired
    private BarCategoryRepository barCategoryRepository;

    @Autowired
    private BarItemRepository barItemRepository;

    public com.horeca.site.models.hotel.services.bar.Bar get(Long hotelId) {
        AvailableServices services = availableServicesService.get(hotelId);
        if (services == null || services.getBreakfast() == null)
            throw new ResourceNotFoundException();

        return services.getBar();
    }

    public Bar addDefaultBar(Long hotelId) {
        AvailableServices services = availableServicesService.addIfDoesntExistAndGet(hotelId);
        if (services.getBar() == null) {
            Bar bar = new Bar();
            bar.setDescription("");
            Price price = new Price();
            price.setCurrency(Currency.EURO);
            price.setValue(new BigDecimal(5));
            bar.setPrice(price);
            bar.setFromHour(localTimeFormatter.parseLocalTime("08:00"));
            bar.setToHour(localTimeFormatter.parseLocalTime("11:00"));

            Set<BarCategory> barCategories = Stream.of(BarCategory.Category.values())
                    .map(categoryName -> {
                        BarCategory category = new BarCategory();
                        category.setCategory(categoryName);
                        return category;
                    })
                    .collect(Collectors.toSet());

            bar.setCategories(barCategories);
            services.setBar(bar);

            AvailableServices updatedServices = availableServicesService.update(services);
            return updatedServices.getBar();
        }
        else {
            throw new BusinessRuleViolationException("A bar service has already been added");
        }
    }

    public BarCategory getCategory(Long hotelId, BarCategory.Category categoryName) {
        return get(hotelId).getCategories().stream()
                .filter(category -> category.getCategory() == categoryName)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("There is no such category"));
    }

    public void addItem(Long hotelId, BarItemUpdate item) {
        BarCategory category = getCategory(hotelId, item.getType());
        if (category != null) {
            BarItem itemNew = new BarItem();
            itemNew.setPrice(item.getPrice());
            itemNew.setAvailable(item.isAvailable());
            itemNew.setName(item.getName());
            category.getItems().add(itemNew);
            barCategoryRepository.save(category);
        }
        else
            throw new ResourceNotFoundException("There is no such category");
    }

    public BarItem updateItem(Long hotelId, BarItemUpdate itemSent) {
        Bar bar = get(hotelId);
        Set<BarCategory> categories = bar.getCategories();

        BarItem found = null;
        BarCategory inCategory = null;
        for (BarCategory barCategory : categories) {
            Optional<BarItem> filteredItem = barCategory.getItems().stream()
                    .filter(item -> item.getId().equals(itemSent.getId()))
                    .findFirst();

            if (filteredItem.isPresent()) {
                found = filteredItem.get();
                inCategory = barCategory;
                break;
            }
        }

        if (found == null)
            throw new ResourceNotFoundException("Could not find an item with such an id");

        found.setPrice(itemSent.getPrice());
        found.setAvailable(itemSent.isAvailable());
        found.setName(itemSent.getName());
        BarItem savedItem = barItemRepository.save(found);

        if (itemSent.getType() != inCategory.getCategory()) {
            BarCategory itemSentCategory = getCategory(hotelId, itemSent.getType());
            itemSentCategory.getItems().add(savedItem);
            barCategoryRepository.save(itemSentCategory);
        }

        return savedItem;
    }

    public void deleteItem(Long hotelId, Long idToDelete) {
        Bar bar = get(hotelId);
        Set<BarCategory> categories = bar.getCategories();

        boolean found = false;
        for (BarCategory barCategory : categories) {
            Optional<BarItem> filteredItem = barCategory.getItems().stream()
                    .filter(item -> item.getId().equals(idToDelete))
                    .findFirst();

            if (filteredItem.isPresent()) {
                barItemRepository.delete(filteredItem.get());
                found = true;
                break;
            }
        }

        if (!found)
            throw new ResourceNotFoundException("Could not find an item with such an id");
    }
}
