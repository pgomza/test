package com.horeca.site.repositories;

import com.horeca.site.models.orders.spa.SpaOrder;
import org.springframework.data.repository.CrudRepository;

public interface SpaOrderRepository extends CrudRepository<SpaOrder, Long> {
}
