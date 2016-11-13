package com.horeca.site.services;

import com.horeca.site.models.guest.Guest;
import com.horeca.site.repositories.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class GuestService {

    @Autowired
    private GuestRepository repository;

    public Set<Guest> getAll() {
        Set<Guest> guests = new HashSet<>();
        for (Guest guest : repository.findAll()) {
            guests.add(guest);
        }
        return guests;
    }

    public Guest get(Long id) {
        return repository.findOne(id);
    }

    public Guest save(@Valid Guest entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.delete(id);
    }
}
