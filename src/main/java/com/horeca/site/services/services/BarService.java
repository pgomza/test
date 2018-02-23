package com.horeca.site.services.services;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.Currency;
import com.horeca.site.models.Price;
import com.horeca.site.models.hotel.services.AvailableServices;
import com.horeca.site.models.hotel.services.bar.Bar;
import com.horeca.site.models.hotel.services.bar.BarCategory;
import com.horeca.site.models.hotel.services.bar.BarItem;
import com.horeca.site.repositories.services.BarItemRepository;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class BarService extends StandardHotelService<Bar, BarCategory>{

    private static final DateTimeFormatter localTimeFormatter = DateTimeFormat.forPattern("HH:mm");

    private final AvailableServicesService availableServicesService;
    private final BarItemRepository itemRepository;

    @Autowired
    public BarService(CrudRepository<Bar, Long> repository,
                      CrudRepository<BarCategory, Long> categoryRepository,
                      AvailableServicesService availableServicesService,
                      BarItemRepository itemRepository) {
        super(repository, categoryRepository);
        this.availableServicesService = availableServicesService;
        this.itemRepository = itemRepository;
    }

    public Bar get(Long hotelId) {
        AvailableServices services = availableServicesService.get(hotelId);
        if (services == null || services.getBar() == null) {
            throw new ResourceNotFoundException();
        }

        return services.getBar();
    }

    public Bar addDefaultBar(Long hotelId) {
        AvailableServices services = availableServicesService.addIfDoesntExistAndGet(hotelId);
        if (services.getBar() == null) {
            Price price = new Price();
            price.setCurrency(Currency.EUR);
            price.setValue(new BigDecimal(5));
            LocalTime fromHour = localTimeFormatter.parseLocalTime("08:00");
            LocalTime toHour = localTimeFormatter.parseLocalTime("11:00");
            Bar bar = new Bar("", new ArrayList<>(), price, fromHour, toHour);

            services.setBar(bar);
            AvailableServices updatedServices = availableServicesService.update(services);
            return updatedServices.getBar();
        }
        else {
            throw new BusinessRuleViolationException("A bar service has already been added");
        }
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
