package com.horeca.site.services.services;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.hotel.services.AvailableServices;
import com.horeca.site.models.hotel.services.restaurantmenu.RestaurantMenu;
import com.horeca.site.models.hotel.services.restaurantmenu.RestaurantMenuCategory;
import com.horeca.site.models.hotel.services.restaurantmenu.RestaurantMenuItem;
import com.horeca.site.repositories.services.RestaurantMenuCategoryRepository;
import com.horeca.site.repositories.services.RestaurantMenuItemRepository;
import com.horeca.site.repositories.services.RestaurantMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class RestaurantMenuService extends StandardHotelService<RestaurantMenu, RestaurantMenuCategory> {

    private final AvailableServicesService availableServicesService;
    private final RestaurantMenuItemRepository itemRepository;

    @Autowired
    public RestaurantMenuService(RestaurantMenuRepository repository,
                                 RestaurantMenuCategoryRepository categoryRepository,
                                 AvailableServicesService availableServicesService,
                                 RestaurantMenuItemRepository itemRepository) {
        super(repository, categoryRepository);
        this.availableServicesService = availableServicesService;
        this.itemRepository = itemRepository;
    }

    public RestaurantMenu get(Long hotelId) {
        AvailableServices services = availableServicesService.get(hotelId);
        if (services == null || services.getRestaurantMenu() == null) {
            throw new ResourceNotFoundException();
        }

        return services.getRestaurantMenu();
    }

    public RestaurantMenu addDefaultRestaurantMenu(Long hotelId) {
        AvailableServices services = availableServicesService.addIfDoesntExistAndGet(hotelId);
        if (services.getRestaurantMenu() == null) {
            RestaurantMenu menu = new RestaurantMenu();
            menu.setDescription("");
            services.setRestaurantMenu(menu);

            AvailableServices updatedServices = availableServicesService.update(services);
            return updatedServices.getRestaurantMenu();
        }
        else {
            throw new BusinessRuleViolationException("A restaurant menu service has already been added");
        }
    }

    public List<RestaurantMenuItem> getItems(Long hotelId, Long categoryId) {
        return getCategory(hotelId, categoryId).getItems();
    }

    public RestaurantMenuItem getItem(Long hotelId, Long categoryId, Long itemId) {
        RestaurantMenuCategory category = getCategory(hotelId, categoryId);
        return category.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findAny()
                .orElseThrow(ResourceNotFoundException::new);
    }

    public RestaurantMenuItem addItem(Long hotelId, Long categoryId, RestaurantMenuItem item) {
        RestaurantMenuCategory category = getCategory(hotelId, categoryId);
        RestaurantMenuItem saved = itemRepository.save(item);
        category.getItems().add(saved);
        updateCategory(hotelId, categoryId, category);
        return saved;
    }

    public RestaurantMenuItem updateItem(Long hotelId, Long categoryId, RestaurantMenuItem itemSent) {
        getItem(hotelId, categoryId, itemSent.getId());
        return itemRepository.save(itemSent);
    }

    public void deleteItem(Long hotelId, Long categoryId, Long itemId) {
        RestaurantMenuCategory category = getCategory(hotelId, categoryId);
        List<RestaurantMenuItem> remainingItems = category.getItems().stream()
                .filter(i -> !Objects.equals(i.getId(), itemId))
                .collect(Collectors.toList());
        category.setItems(remainingItems);
        updateCategory(hotelId, categoryId, category);
    }
}
