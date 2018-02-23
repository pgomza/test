package com.horeca.site.services.services;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.Currency;
import com.horeca.site.models.Price;
import com.horeca.site.models.hotel.services.AvailableServices;
import com.horeca.site.models.hotel.services.breakfast.Breakfast;
import com.horeca.site.models.hotel.services.breakfast.BreakfastCategory;
import com.horeca.site.models.hotel.services.breakfast.BreakfastItem;
import com.horeca.site.repositories.services.BreakfastCategoryRepository;
import com.horeca.site.repositories.services.BreakfastItemRepository;
import com.horeca.site.repositories.services.BreakfastRepository;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class BreakfastService extends StandardHotelService<Breakfast, BreakfastCategory> {

    private static final DateTimeFormatter localTimeFormatter = DateTimeFormat.forPattern("HH:mm");

    private final AvailableServicesService availableServicesService;
    private final BreakfastItemRepository itemRepository;

    @Autowired
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
        if (services == null || services.getBreakfast() == null) {
            throw new ResourceNotFoundException();
        }

        return services.getBreakfast();
    }

    public Breakfast addDefaultBreakfast(Long hotelId) {
        AvailableServices services = availableServicesService.addIfDoesntExistAndGet(hotelId);
        if (services.getBreakfast() == null) {
            Price price = new Price();
            price.setCurrency(Currency.EUR);
            price.setValue(new BigDecimal(5));
            LocalTime fromHour = localTimeFormatter.parseLocalTime("08:00");
            LocalTime toHour = localTimeFormatter.parseLocalTime("11:00");
            Breakfast breakfast = new Breakfast("", new ArrayList<>(), price, fromHour, toHour);

            services.setBreakfast(breakfast);
            AvailableServices updatedServices = availableServicesService.update(services);
            return updatedServices.getBreakfast();
        }
        else {
            throw new BusinessRuleViolationException("A breakfast service has already been added");
        }
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
