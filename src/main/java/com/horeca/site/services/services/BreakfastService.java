package com.horeca.site.services.services;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.hotel.services.AvailableServices;
import com.horeca.site.models.hotel.services.breakfast.Breakfast;
import com.horeca.site.models.hotel.services.breakfast.BreakfastCategory;
import com.horeca.site.models.hotel.services.breakfast.BreakfastItem;
import com.horeca.site.repositories.services.BreakfastCategoryRepository;
import com.horeca.site.repositories.services.BreakfastItemRepository;
import com.horeca.site.repositories.services.BreakfastRepository;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class BreakfastService extends StandardHotelService<Breakfast, BreakfastCategory> {

    private static final DateTimeFormatter localTimeFormatter = DateTimeFormat.forPattern("HH:mm");

    private AvailableServicesService availableServicesService;
    private BreakfastItemRepository itemRepository;

    public BreakfastService(BreakfastRepository repository,
                      BreakfastCategoryRepository categoryRepository,
                      AvailableServicesService availableServicesService,
                      BreakfastItemRepository itemRepository) {
        super(repository, categoryRepository);
        this.availableServicesService = availableServicesService;
        this.itemRepository = itemRepository;
    }

    public Breakfast get(Long hotelId) {
        AvailableServices services = availableServicesService.get(hotelId);
        return services.getBreakfast();
    }

    public List<BreakfastItem> getItems(Long hotelId, Long categoryId) {
        return getCategory(hotelId, categoryId).getItems();
    }

    public BreakfastItem getItem(Long hotelId, Long categoryId, Long itemId) {
        BreakfastCategory category = getCategory(hotelId, categoryId);
        return category.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findAny()
                .orElseThrow(ResourceNotFoundException::new);
    }

    public BreakfastItem addItem(Long hotelId, Long categoryId, BreakfastItem item) {
        BreakfastCategory category = getCategory(hotelId, categoryId);
        BreakfastItem saved = itemRepository.save(item);
        category.getItems().add(saved);
        updateCategory(hotelId, categoryId, category);
        return saved;
    }

    public BreakfastItem updateItem(Long hotelId, Long categoryId, BreakfastItem itemSent) {
        getItem(hotelId, categoryId, itemSent.getId());
        return itemRepository.save(itemSent);
    }

    public void deleteItem(Long hotelId, Long categoryId, Long itemId) {
        BreakfastCategory category = getCategory(hotelId, categoryId);
        List<BreakfastItem> remainingItems = category.getItems().stream()
                .filter(i -> !Objects.equals(i.getId(), itemId))
                .collect(Collectors.toList());
        category.setItems(remainingItems);
        updateCategory(hotelId, categoryId, category);
    }
}
