package com.horeca.site.services.services;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.Currency;
import com.horeca.site.models.Price;
import com.horeca.site.models.hotel.services.AvailableServices;
import com.horeca.site.models.hotel.services.petcare.PetCare;
import com.horeca.site.models.hotel.services.petcare.PetCareItem;
import com.horeca.site.repositories.services.PetCareItemRepository;
import com.horeca.site.repositories.services.PetCareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PetCareService {

    @Autowired
    private AvailableServicesService availableServicesService;

    @Autowired
    private PetCareRepository repository;

    @Autowired
    private PetCareItemRepository itemRepository;

    public PetCare get(Long hotelId) {
        AvailableServices services = availableServicesService.get(hotelId);
        if (services == null || services.getPetCare() == null)
            throw new ResourceNotFoundException();
        return services.getPetCare();
    }

    public PetCare addDefaultPetCare(Long hotelId) {
        AvailableServices services = availableServicesService.addIfDoesntExistAndGet(hotelId);
        if (services.getPetCare() == null) {
            PetCare petCare = new PetCare();
            petCare.setDescription("");
            Price petCarePrice = new Price();
            petCarePrice.setCurrency(Currency.EURO);
            petCarePrice.setValue(new BigDecimal(5));
            petCare.setPrice(petCarePrice);

            services.setPetCare(petCare);
            AvailableServices updatedServices = availableServicesService.update(services);
            return updatedServices.getPetCare();
        }
        else {
            throw new BusinessRuleViolationException("A petcare service has already been added");
        }
    }

    public List<PetCareItem> getItems(Long hotelId) {
        return get(hotelId).getItems();
    }

    public PetCareItem getItem(Long hotelId, Long itemId) {
        Optional<PetCareItem> itemOptional = getItems(hotelId).stream()
                .filter(i -> i.getId().equals(itemId))
                .findAny();

        if (itemOptional.isPresent()) {
            return itemOptional.get();
        }
        else {
            throw new ResourceNotFoundException();
        }
    }

    public PetCareItem addItem(Long hotelId, PetCareItem item) {
        PetCare petCare = get(hotelId);
        PetCareItem savedItem = itemRepository.save(item);
        petCare.getItems().add(item);
        repository.save(petCare);
        return savedItem;
    }

    public PetCareItem updateItem(Long hotelId, PetCareItem item) {
        getItem(hotelId, item.getId());
        return itemRepository.save(item);
    }

    public void deleteItem(Long hotelId, Long itemId) {
        PetCare petCare = get(hotelId);
        List<PetCareItem> remaining = petCare.getItems().stream()
                .filter(i -> !i.getId().equals(itemId))
                .collect(Collectors.toList());
        petCare.setItems(remaining);
        repository.save(petCare);
    }
}
