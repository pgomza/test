package com.horeca.site.repositories;

import com.horeca.site.models.guest.Guest;
import org.springframework.data.repository.CrudRepository;

public interface GuestRepository extends CrudRepository<Guest, Long> {
}
