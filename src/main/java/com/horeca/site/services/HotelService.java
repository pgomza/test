package com.horeca.site.services;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.repositories.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HotelService {

    @Autowired
    private HotelRepository repository;

    public Iterable<Hotel> getAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Hotel get(Long id) {
        Hotel hotel = repository.findOne(id);
        if (hotel == null)
            throw new ResourceNotFoundException();

        return hotel;
    }

    public Hotel add(Hotel hotel) {
        return repository.save(hotel);
    }

    public Hotel update(Long id, Hotel newOne) {
        Hotel oldOne = get(id);
        newOne.setId(id);
        Hotel changed = repository.save(newOne);

        return changed;
    }

    public void delete(Long id) {
        Hotel toDelete = get(id);
        repository.delete(toDelete);
    }
}
