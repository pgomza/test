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

import java.util.Optional;

@Service
@Transactional
public class RestaurantMenuService {

    @Autowired
    private AvailableServicesService availableServicesService;

    @Autowired
    private RestaurantMenuRepository repository;

    @Autowired
    private RestaurantMenuCategoryRepository categoryRepository;

    @Autowired
    private RestaurantMenuItemRepository itemRepository;

    public RestaurantMenu get(Long hotelId) {
        AvailableServices services = availableServicesService.get(hotelId);
        if (services == null || services.getRestaurantMenu() == null) {
            throw new ResourceNotFoundException();
        }

        return services.getRestaurantMenu();
    }

    public RestaurantMenu update(RestaurantMenu updated) {
        return repository.save(updated);
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

    public RestaurantMenuCategory getCategory(Long hotelId, Long categoryId) {
        return get(hotelId).getCategories().stream()
                .filter(c -> c.getId().equals(categoryId))
                .findFirst()
                .orElseThrow(ResourceNotFoundException::new);
    }

    public RestaurantMenuCategory addCategory(Long hotelId, RestaurantMenuCategory category) {
        RestaurantMenu restaurantMenu = get(hotelId);
        Optional<RestaurantMenuCategory> matchingCategory = restaurantMenu.getCategories().stream()
                .filter(c -> c.getName().equalsIgnoreCase(category.getName()))
                .findAny();

        if (matchingCategory.isPresent())
            throw new BusinessRuleViolationException("A category with such a name already exists");

        restaurantMenu.getCategories().add(category);
        update(restaurantMenu);
        return restaurantMenu.getCategories().stream()
                .filter(c -> c.getName().equalsIgnoreCase(category.getName()))
                .findFirst()
                .get();
    }

    public RestaurantMenuCategory updateCategory(Long hotelId, RestaurantMenuCategory updated) {
        getCategory(hotelId, updated.getId());
        return categoryRepository.save(updated);
    }

    public void deleteCategory(Long hotelId, Long categoryId) {
        getCategory(hotelId, categoryId);
        categoryRepository.delete(categoryId);
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
        updateCategory(hotelId, category);
        return saved;
    }

    public RestaurantMenuItem updateItem(Long hotelId, Long categoryId, RestaurantMenuItem itemSent) {
        getItem(hotelId, categoryId, itemSent.getId());
        return itemRepository.save(itemSent);
    }

    public void deleteItem(Long hotelId, Long categoryId, Long itemId) {
        getItem(hotelId, categoryId, itemId);
        itemRepository.delete(itemId);
    }
}
