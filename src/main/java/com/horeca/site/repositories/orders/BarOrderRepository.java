package com.horeca.site.repositories.orders;

import com.horeca.site.models.orders.bar.BarOrder;
import org.springframework.data.repository.CrudRepository;

public interface BarOrderRepository extends CrudRepository<BarOrder, Long> {
}
