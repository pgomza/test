package com.horeca.site.services.services;

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

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

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
        return services.getRestaurantMenu();
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
        Iterator<RestaurantMenuItem> iterator = category.getItems().iterator();
        boolean isRemoved = false;
        while (iterator.hasNext() && !isRemoved) {
            RestaurantMenuItem item = iterator.next();
            if (Objects.equals(item.getId(), itemId)) {
                category.getItems().remove(item);
                isRemoved = true;
            }
        }

        if (!isRemoved) {
            throw new ResourceNotFoundException();
        }

        updateCategory(hotelId, categoryId, category);
    }
}
