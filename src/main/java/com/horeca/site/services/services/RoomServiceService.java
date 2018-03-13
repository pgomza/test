package com.horeca.site.services.services;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.hotel.services.AvailableServices;
import com.horeca.site.models.hotel.services.roomservice.RoomService;
import com.horeca.site.models.hotel.services.roomservice.RoomServiceCategory;
import com.horeca.site.models.hotel.services.roomservice.RoomServiceItem;
import com.horeca.site.models.hotel.services.roomservice.RoomServiceItemUpdate;
import com.horeca.site.repositories.services.RoomServiceCategoryRepository;
import com.horeca.site.repositories.services.RoomServiceItemRepository;
import com.horeca.site.repositories.services.RoomServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class RoomServiceService extends GenericHotelService<RoomService> {

    private AvailableServicesService availableServicesService;
    private RoomServiceCategoryRepository roomServiceCategoryRepository;
    private RoomServiceItemRepository breakfastItemRepository;

    @Autowired
    public RoomServiceService(RoomServiceRepository repository,
                              AvailableServicesService availableServicesService,
                              RoomServiceCategoryRepository roomServiceCategoryRepository,
                              RoomServiceItemRepository breakfastItemRepository) {
        super(repository);
        this.availableServicesService = availableServicesService;
        this.roomServiceCategoryRepository = roomServiceCategoryRepository;
        this.breakfastItemRepository = breakfastItemRepository;
    }

    public RoomService get(Long hotelId) {
        AvailableServices services = availableServicesService.get(hotelId);
        return services.getRoomService();
    }

    private RoomServiceCategory getCategory(Long hotelId, RoomServiceCategory.Category category) {
        RoomService roomService = get(hotelId);
        Set<RoomServiceCategory> categories = roomService.getCategories();
        for (RoomServiceCategory roomServiceCategory : categories) {
            if (roomServiceCategory.getCategory() == category)
                return roomServiceCategory;
        }
        throw new ResourceNotFoundException("There is no such category");
    }

    public void addItem(Long hotelId, RoomServiceItemUpdate item) {
        RoomServiceCategory category = getCategory(hotelId, item.getType());
        if (category != null) {
            RoomServiceItem itemNew = new RoomServiceItem();
            itemNew.setPrice(item.getPrice());
            itemNew.setName(item.getName());
            category.getItems().add(itemNew);
            roomServiceCategoryRepository.save(category);
        }
        else {
            throw new ResourceNotFoundException("There is no such category");
        }
    }

    public void updateItem(Long hotelId, RoomServiceItemUpdate item) {
        RoomService roomService = get(hotelId);
        Set<RoomServiceCategory> categories = roomService.getCategories();

        RoomServiceItem foundItem = null;
        for (RoomServiceCategory category : categories) {
            Set<RoomServiceItem> items = category.getItems();

            Set<RoomServiceItem> remaining = new HashSet<>();
            for (RoomServiceItem roomServiceItem : items) {
                if (!roomServiceItem.getId().equals(item.getId()))
                    remaining.add(roomServiceItem);
                else
                    foundItem = roomServiceItem;
            }

            category.setItems(remaining);
            roomServiceCategoryRepository.save(category);
        }

        RoomServiceCategory category = getCategory(hotelId, item.getType());
        foundItem.setPrice(item.getPrice());
        foundItem.setName(item.getName());
        category.getItems().add(foundItem);
        roomServiceCategoryRepository.save(category);
    }

    public void deleteItem(Long itemId) {
        RoomServiceItem existingItem = breakfastItemRepository.findOne(itemId);
        if (existingItem != null) {
            breakfastItemRepository.delete(itemId);
        }
        else
            throw new ResourceNotFoundException("Could not find an item with such an id");
    }
}
