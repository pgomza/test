package com.horeca.site.repositories.services;

import com.horeca.site.models.hotel.services.tableordering.TableOrdering;
import org.springframework.data.repository.CrudRepository;

public interface TableOrderingRepository extends CrudRepository<TableOrdering, Long>{
}
