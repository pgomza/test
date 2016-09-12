package com.horeca.site.repositories;

import com.horeca.site.models.Stay;
import org.springframework.data.repository.CrudRepository;

public interface StayRepository extends CrudRepository<Stay, String> {

    Stay findByPin(String pin);
}
