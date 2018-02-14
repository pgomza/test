package com.horeca.site.services.services;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.hotel.services.AvailableServices;
import com.horeca.site.models.hotel.services.restaurantmenu.*;
import com.horeca.site.repositories.services.RestaurantMenuCategoryRepository;
import com.horeca.site.repositories.services.RestaurantMenuItemRepository;
import com.horeca.site.repositories.services.RestaurantMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class RestaurantMenuService extends GenericHotelService<RestaurantMenu> {

    private AvailableServicesService availableServicesService;
    private RestaurantMenuCategoryRepository categoryRepository;
    private RestaurantMenuItemRepository itemRepository;

    @Autowired
    public RestaurantMenuService(RestaurantMenuRepository repository,
                                 AvailableServicesService availableServicesService,
                                 RestaurantMenuCategoryRepository categoryRepository,
                                 RestaurantMenuItemRepository itemRepository) {
        super(repository);
        this.availableServicesService = availableServicesService;
        this.categoryRepository = categoryRepository;
        this.itemRepository = itemRepository;
    }

    public RestaurantMenu get(Long hotelId) {
        AvailableServices services = availableServicesService.get(hotelId);
        return services.getRestaurantMenu();
    }

    public RestaurantMenu update(Long hotelId, RestaurantMenu updated) {
        RestaurantMenu menu = get(hotelId);
        updated.setId(menu.getId());
        return repository.save(updated);
    }

    public RestaurantMenu patch(Long hotelId, RestaurantMenuPATCH patch) {
        RestaurantMenu menu = get(hotelId);
        menu.setDescription(patch.getDescription());
        return repository.save(menu);
    }

    public List<RestaurantMenuCategory> getCategories(Long hotelId) {
        return get(hotelId).getCategories();
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
        update(hotelId, restaurantMenu);
        return restaurantMenu.getCategories().stream()
                .filter(c -> c.getName().equalsIgnoreCase(category.getName()))
                .findFirst()
                .get();
    }

    public RestaurantMenuCategory updateCategory(Long hotelId, Long categoryId, RestaurantMenuCategory updated) {
        getCategory(hotelId, categoryId);
        updated.setId(categoryId);
        return categoryRepository.save(updated);
    }

    public RestaurantMenuCategory patchCategory(Long hotelId, Long categoryId, RestaurantMenuCategoryPATCH patch) {
        RestaurantMenuCategory category = getCategory(hotelId, categoryId);
        category.setName(patch.getName());
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long hotelId, Long categoryId) {
        RestaurantMenu menu = get(hotelId);
        List<RestaurantMenuCategory> remainingCategories = menu.getCategories().stream()
                .filter(c -> c.getId() != categoryId)
                .collect(Collectors.toList());
        menu.setCategories(remainingCategories);
        update(hotelId, menu);
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
                .filter(i -> i.getId() != itemId)
                .collect(Collectors.toList());
        category.setItems(remainingItems);
        updateCategory(hotelId, categoryId, category);
    }
}
