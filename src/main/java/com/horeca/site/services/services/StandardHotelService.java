package com.horeca.site.services.services;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.hotel.services.StandardServiceCategoryModel;
import com.horeca.site.models.hotel.services.StandardServiceCategoryModelPatch;
import com.horeca.site.models.hotel.services.StandardServiceModel;
import com.horeca.site.models.hotel.services.StandardServiceModelPatch;
import org.springframework.data.repository.CrudRepository;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class StandardHotelService<T extends StandardServiceModel<S>, S extends StandardServiceCategoryModel>
    extends GenericHotelService<T> {

    private final CrudRepository<S, Long> categoryRepository;

    public StandardHotelService(CrudRepository<T, Long> repository,
                                CrudRepository<S, Long> categoryRepository) {
        super(repository);
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
        Iterator<S> iterator = service.getCategories().iterator();

        boolean isRemoved = false;
        while (iterator.hasNext() && !isRemoved) {
            S category = iterator.next();
            if (Objects.equals(category.getId(), categoryId)) {
                service.getCategories().remove(category);
                isRemoved = true;
            }
        }

        if (!isRemoved) {
            throw new ResourceNotFoundException();
        }

        update(hotelId, service);
    }
}
