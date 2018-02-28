package com.horeca.site.services.services;

import com.horeca.site.handlers.HotelId;
import com.horeca.site.handlers.MinSubscriptionLevel;
import com.horeca.site.models.hotel.services.ServiceAvailability;
import org.springframework.data.repository.CrudRepository;

public abstract class GenericHotelService<T extends ServiceAvailability> {

    protected CrudRepository<T, Long> repository;

    public GenericHotelService(CrudRepository<T, Long> repository) {
        this.repository = repository;
    }

    public abstract T get(Long hotelId);

    @MinSubscriptionLevel(2)
    public T updateAvailability(@HotelId Long hotelId, boolean available) {
        T entity = get(hotelId);
        entity.setAvailable(available);
        return repository.save(entity);
    }
}
