package com.horeca.site.services.services;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.hotel.services.StandardServiceCategoryModel;
import com.horeca.site.models.hotel.services.StandardServiceCategoryModelPatch;
import com.horeca.site.models.hotel.services.StandardServiceModel;
import com.horeca.site.models.hotel.services.StandardServiceModelPatch;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class StandardHotelService<T extends StandardServiceModel<S>, S extends StandardServiceCategoryModel> {

    private final CrudRepository<T, Long> repository;
    private final CrudRepository<S, Long> categoryRepository;

    public StandardHotelService(CrudRepository<T, Long> repository, CrudRepository<S, Long> categoryRepository) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
    }

    public abstract T get(Long hotelId);

    public T update(Long hotelId, T updated) {
        T service = get(hotelId);
        updated.setId(service.getId());
        return repository.save(updated);
    }

    public T patch(Long hotelId, StandardServiceModelPatch patch) {
        T service = get(hotelId);
        service.setDescription(patch.getDescription());
        return repository.save(service);
    }

    public List<S> getCategories(Long hotelId) {
        return get(hotelId).getCategories();
    }

    public S getCategory(Long hotelId, Long categoryId) {
        return get(hotelId).getCategories().stream()
                .filter(c -> c.getId().equals(categoryId))
                .findFirst()
                .orElseThrow(ResourceNotFoundException::new);
    }

    public S addCategory(Long hotelId, S category) {
        T service = get(hotelId);
        Optional<S> matchingCategory = service.getCategories().stream()
                .filter(c -> c.getName().equalsIgnoreCase(category.getName()))
                .findAny();

        if (matchingCategory.isPresent()) {
            throw new BusinessRuleViolationException("A category with such a name already exists");
        }

        service.getCategories().add(category);
        update(hotelId, service);
        return service.getCategories().stream()
                .filter(c -> c.getName().equalsIgnoreCase(category.getName()))
                .findFirst()
                .get();
    }

    public S updateCategory(Long hotelId, Long categoryId, S updated) {
        getCategory(hotelId, categoryId);
        updated.setId(categoryId);
        return categoryRepository.save(updated);
    }

    public S patchCategory(Long hotelId, Long categoryId, StandardServiceCategoryModelPatch patch) {
        S category = getCategory(hotelId, categoryId);
        category.setName(patch.getName());
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long hotelId, Long categoryId) {
        T service = get(hotelId);
        List<S> remainingCategories = service.getCategories().stream()
                .filter(c -> !Objects.equals(c.getId(), categoryId))
                .collect(Collectors.toList());
        service.setCategories(remainingCategories);
        update(hotelId, service);
    }
}
