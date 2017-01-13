package com.horeca.site.repositories.orders;

import com.horeca.site.models.orders.roomservice.RoomServiceOrder;
import org.springframework.data.repository.CrudRepository;

public interface RoomServiceOrderRepository extends CrudRepository<RoomServiceOrder, Long> {
}
