package com.horeca.site.repositories.orders;

import com.horeca.site.models.orders.breakfast.BreakfastOrder;
import org.springframework.data.repository.CrudRepository;

public interface BreakfastOrderRepository extends CrudRepository<BreakfastOrder, Long> {
}
