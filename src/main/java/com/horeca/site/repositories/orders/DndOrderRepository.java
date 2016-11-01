package com.horeca.site.repositories.orders;

import com.horeca.site.models.orders.dnd.DndOrder;
import org.springframework.data.repository.CrudRepository;

public interface DndOrderRepository extends CrudRepository<DndOrder, Long> {
}
