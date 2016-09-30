package com.horeca.site.repositories;

import com.horeca.site.models.orders.carpark.CarParkOrder;
import org.springframework.data.repository.CrudRepository;

public interface CarParkOrderRepository extends CrudRepository<CarParkOrder, Long> {
}
