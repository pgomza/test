package com.horeca.site.services.services;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.Currency;
import com.horeca.site.models.Price;
import com.horeca.site.models.hotel.services.AvailableServices;
import com.horeca.site.models.hotel.services.breakfast.Breakfast;
import com.horeca.site.models.hotel.services.breakfast.BreakfastCategory;
import com.horeca.site.models.hotel.services.breakfast.BreakfastItem;
import com.horeca.site.models.hotel.services.breakfast.BreakfastItemUpdate;
import com.horeca.site.repositories.services.BreakfastCategoryRepository;
import com.horeca.site.repositories.services.BreakfastItemRepository;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class BreakfastService {

    private static final DateTimeFormatter localTimeFormatter = DateTimeFormat.forPattern("HH:mm");

    @Autowired
    private AvailableServicesService availableServicesService;

    @Autowired
    private BreakfastCategoryRepository breakfastCategoryRepository;

    @Autowired
    private BreakfastItemRepository breakfastItemRepository;

    public Breakfast get(Long hotelId) {
        AvailableServices services = availableServicesService.get(hotelId);
        if (services == null || services.getBreakfast() == null)
            throw new ResourceNotFoundException();

        return services.getBreakfast();
    }

    public Breakfast addDefaultBreakfast(Long hotelId) {
        AvailableServices services = availableServicesService.get(hotelId);
        if (services.getBreakfast() == null) {
            Breakfast breakfast = new Breakfast();
            breakfast.setDescription("");
            breakfast.setFromHour(localTimeFormatter.parseLocalTime("08:00"));
            breakfast.setToHour(localTimeFormatter.parseLocalTime("11:00"));
            Price breakfastPrice = new Price();
            breakfastPrice.setCurrency(Currency.EUR);
            breakfastPrice.setValue(new BigDecimal(5));
            breakfast.setPrice(breakfastPrice);

            BreakfastCategory dishCategory = new BreakfastCategory();
            dishCategory.setCategory(BreakfastCategory.Category.DISH);
            BreakfastCategory drinkCategory = new BreakfastCategory();
            drinkCategory.setCategory(BreakfastCategory.Category.DRINK);
            Set<BreakfastCategory> categories = new HashSet<>();
            categories.add(dishCategory);
            categories.add(drinkCategory);
            breakfast.setCategories(categories);

            services.setBreakfast(breakfast);
            AvailableServices updatedServices = availableServicesService.update(services);
            return updatedServices.getBreakfast();
        }
        else {
            throw new BusinessRuleViolationException("A breakfast service has already been added");
        }
    }

    public BreakfastCategory getCategory(Long hotelId, BreakfastCategory.Category category) {
        Breakfast breakfast = get(hotelId);
        Set<BreakfastCategory> categories = breakfast.getCategories();
        for (BreakfastCategory breakfastCategory : categories) {
            if (breakfastCategory.getCategory() == category)
                return breakfastCategory;
        }
        throw new ResourceNotFoundException("There is no such category");
    }

    public void addItem(Long hotelId, BreakfastItemUpdate item) {
        BreakfastCategory category = getCategory(hotelId, item.getType());
        if (category != null) {
            BreakfastItem itemNew = new BreakfastItem();
            itemNew.setPrice(item.getPrice());
            itemNew.setAvailable(item.isAvailable());
            itemNew.setName(item.getName());
            category.getItems().add(itemNew);
            breakfastCategoryRepository.save(category);
        }
        else
            throw new ResourceNotFoundException("There is no such category");
    }

    public void updateItem(Long hotelId, BreakfastItemUpdate item) {
        Breakfast breakfast = get(hotelId);
        Set<BreakfastCategory> categories = breakfast.getCategories();

        BreakfastItem foundItem = null;
        for (BreakfastCategory category : categories) {
            Set<BreakfastItem> items = category.getItems();

            Set<BreakfastItem> remaining = new HashSet<>();
            for (BreakfastItem breakfastItem : items) {
                if (!breakfastItem.getId().equals(item.getId()))
                    remaining.add(breakfastItem);
                else
                    foundItem = breakfastItem;
            }

            category.setItems(remaining);
            breakfastCategoryRepository.save(category);
        }

        BreakfastCategory category = getCategory(hotelId, item.getType());
        foundItem.setPrice(item.getPrice());
        foundItem.setAvailable(item.isAvailable());
        foundItem.setName(item.getName());
        category.getItems().add(foundItem);
        breakfastCategoryRepository.save(category);
    }

    public void deleteItem(Long itemId) {
        BreakfastItem existingItem = breakfastItemRepository.findOne(itemId);
        if (existingItem != null) {
            breakfastItemRepository.delete(itemId);
        }
        else
            throw new ResourceNotFoundException("Could not find an item with such an id");
    }
}
