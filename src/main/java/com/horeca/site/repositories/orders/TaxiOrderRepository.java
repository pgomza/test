package com.horeca.site.repositories.orders;

import com.horeca.site.models.orders.taxi.TaxiOrder;
import org.springframework.data.repository.CrudRepository;

public interface TaxiOrderRepository extends CrudRepository<TaxiOrder, Long> {
}
