package com.horeca.site.services.services;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.hotel.services.AvailableServices;
import com.horeca.site.models.hotel.services.bar.Bar;
import com.horeca.site.models.hotel.services.bar.BarCategory;
import com.horeca.site.models.hotel.services.bar.BarItem;
import com.horeca.site.repositories.services.BarCategoryRepository;
import com.horeca.site.repositories.services.BarItemRepository;
import com.horeca.site.repositories.services.BarRepository;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class BarService extends StandardHotelService<Bar, BarCategory> {

    private static final DateTimeFormatter localTimeFormatter = DateTimeFormat.forPattern("HH:mm");

    private AvailableServicesService availableServicesService;
    private BarItemRepository itemRepository;

    public BarService(BarRepository repository,
                      BarCategoryRepository categoryRepository,
                      AvailableServicesService availableServicesService,
                      BarItemRepository itemRepository) {
        super(repository, categoryRepository);
        this.availableServicesService = availableServicesService;
        this.itemRepository = itemRepository;
    }

    public Bar get(Long hotelId) {
        AvailableServices services = availableServicesService.get(hotelId);
        return services.getBar();
    }

    public List<BarItem> getItems(Long hotelId, Long categoryId) {
        return getCategory(hotelId, categoryId).getItems();
    }

    public BarItem getItem(Long hotelId, Long categoryId, Long itemId) {
        BarCategory category = getCategory(hotelId, categoryId);
        return category.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findAny()
                .orElseThrow(ResourceNotFoundException::new);
    }

    public BarItem addItem(Long hotelId, Long categoryId, BarItem item) {
        BarCategory category = getCategory(hotelId, categoryId);
        BarItem saved = itemRepository.save(item);
        category.getItems().add(saved);
        updateCategory(hotelId, categoryId, category);
        return saved;
    }

    public BarItem updateItem(Long hotelId, Long categoryId, BarItem itemSent) {
        getItem(hotelId, categoryId, itemSent.getId());
        return itemRepository.save(itemSent);
    }

    public void deleteItem(Long hotelId, Long categoryId, Long itemId) {
        BarCategory category = getCategory(hotelId, categoryId);
        List<BarItem> remainingItems = category.getItems().stream()
                .filter(i -> !Objects.equals(i.getId(), itemId))
                .collect(Collectors.toList());
        category.setItems(remainingItems);
        updateCategory(hotelId, categoryId, category);
    }
}
