package com.horeca.site.services.services;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.hotel.services.breakfast.*;
import com.horeca.site.repositories.services.BreakfastCategoryRepository;
import com.horeca.site.repositories.services.BreakfastItemRepository;
import com.horeca.site.repositories.services.BreakfastRepository;
import com.horeca.site.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class BreakfastService {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private BreakfastRepository breakfastRepository;

    @Autowired
    private BreakfastCategoryRepository breakfastCategoryRepository;

    @Autowired
    private BreakfastItemRepository breakfastItemRepository;

    public Breakfast get(Long hotelId) {
        Hotel hotel = hotelService.get(hotelId);
        return hotel.getAvailableServices().getBreakfast();
    }

    public BreakfastView getView(Long hotelId, String preferredLanguage) {
        Breakfast breakfast = get(hotelId);
        return breakfast.toView(preferredLanguage, hotelService.get(hotelId).getDefaultTranslation());
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

    public BreakfastCategoryView getCategoryView(Long hotelId, BreakfastCategory.Category category,
                                                 String preferredLanguage) {
        String defaultLanguage = hotelService.get(hotelId).getDefaultTranslation();
        BreakfastCategory breakfastCategory = getCategory(hotelId, category);
        return breakfastCategory.toView(preferredLanguage, defaultLanguage);
    }

    public void addItem(Long hotelId, BreakfastItemUpdate item) {
        BreakfastCategory category = getCategory(hotelId, item.getType());
        if (category != null) {
            BreakfastItem itemNew = new BreakfastItem();
            itemNew.setPrice(item.getPrice());
            itemNew.setTranslations(getDefaultTranslationSet(item.getName()));
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
        foundItem.setTranslations(getDefaultTranslationSet(item.getName()));
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

    private Set<BreakfastItemTranslation> getDefaultTranslationSet(String name) {
        BreakfastItemTranslation translation = new BreakfastItemTranslation();
        translation.setLanguage("en");
        translation.setName(name);
        Set<BreakfastItemTranslation> translationSet = new HashSet<>();
        translationSet.add(translation);
        return translationSet;
    }
}
