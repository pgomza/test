package com.horeca.site.repositories.orders;

import com.horeca.site.models.orders.rental.RentalOrder;
import org.springframework.data.repository.CrudRepository;

public interface RentalOrderRepository extends CrudRepository<RentalOrder, Long> {
}
