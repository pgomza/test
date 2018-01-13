package com.horeca.site.services;

import com.horeca.site.models.logs.ClientLogEntry;
import com.horeca.site.models.logs.ClientLogEntry.Level;
import com.horeca.site.repositories.ClientLoggingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.horeca.site.models.logs.ClientLogEntry.Level.INFO;

@Service
@Transactional
public class ClientLoggingService {

    @Autowired
    private ClientLoggingRepository repository;

    public Page<ClientLogEntry> getEntries(Pageable pageable) {
        List<Long> idsSorted = repository.getIdsSorted();
        List<ClientLogEntry> fetched = fetchBatch(idsSorted, pageable);
        return new PageImpl<>(fetched, pageable, idsSorted.size());
    }

    public Page<ClientLogEntry> getEntriesByLevels(Pageable pageable, Set<Level> levels) {
        List<Long> idsByLevels = repository.getIdsByLevelsSorted(levels);
        List<ClientLogEntry> fetched = fetchBatch(idsByLevels, pageable);
        return new PageImpl<>(fetched, pageable, idsByLevels.size());
    }

    private List<ClientLogEntry> fetchBatch(List<Long> ids, Pageable pageable) {
        int totalCount = ids.size();
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        int fromIndex = Math.max(pageNumber * pageSize, 0);
        int toIndex = Math.max(Math.min(fromIndex + pageSize, totalCount), 0);

        List<Long> idsToFetch = ids.subList(fromIndex, toIndex);

        return idsToFetch.stream()
                .map(id -> repository.findOne(id))
                .collect(Collectors.toList());
    }

    public ClientLogEntry add(ClientLogEntry entry) {
        if (entry.getLevel() == null) {
            entry.setLevel(INFO);
        }
        // make sure we don't update existing entries
        entry.setId(null);
        return repository.save(entry);
    }
}
