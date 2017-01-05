package com.horeca.site.repositories.orders;

import com.horeca.site.models.orders.housekeeping.HousekeepingOrder;
import org.springframework.data.repository.CrudRepository;

public interface HousekeepingOrderRepository extends CrudRepository<HousekeepingOrder, Long> {
}
