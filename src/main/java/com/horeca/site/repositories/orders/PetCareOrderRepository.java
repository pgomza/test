package com.horeca.site.repositories.orders;

import com.horeca.site.models.orders.petcare.PetCareOrder;
import org.springframework.data.repository.CrudRepository;

public interface PetCareOrderRepository extends CrudRepository<PetCareOrder, Long> {
}
