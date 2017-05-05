package com.horeca.site.repositories.orders;

import com.horeca.site.models.orders.tableordering.TableOrderingOrder;
import org.springframework.data.repository.CrudRepository;

public interface TableOrderingOrderRepository extends CrudRepository<TableOrderingOrder, Long> {
}
