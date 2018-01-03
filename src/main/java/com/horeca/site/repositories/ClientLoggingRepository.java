package com.horeca.site.repositories;

import com.horeca.site.models.logs.ClientLogEntry;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ClientLoggingRepository extends PagingAndSortingRepository<ClientLogEntry, Long> {

    @Query("select e.id from ClientLogEntry e order by e.createdAt desc")
    List<Long> getIdsSorted();

    @Query("select e.id from ClientLogEntry e where e.level in :levels order by e.createdAt desc")
    List<Long> getIdsByLevelsSorted(@Param("levels") Set<ClientLogEntry.Level> levels);
}
