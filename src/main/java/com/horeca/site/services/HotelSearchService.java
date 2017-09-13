package com.horeca.site.services;

import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.repositories.HotelRepository;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Service
@Transactional
public class HotelSearchService {

    @Autowired
    private HotelRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Long> getAll() {
        return repository.getAll();
    }

    /**
     * Returns ids of the hotels for which 'isMarkedAsDeleted' has been true for at least 7 days
     */
    public List<Long> getMarkedAndOutdated() {
        List<Long> markedAsDeleted = repository.getAllMarkedAsDeleted();
        AuditReader reader = AuditReaderFactory.get(entityManager);

        final long weekAgoTimestamp = Instant.now().minus(Duration.standardDays(7L)).getMillis();

        List<Long> remaining = new ArrayList<>();
        for (Long hotelId : markedAsDeleted) {
            boolean shouldRemainAsOutdated = true;

            AuditQuery query = reader.createQuery()
                    .forRevisionsOfEntity(Hotel.class, false, false)
                    .add(AuditEntity.id().eq(hotelId))
                    .add(AuditEntity.revisionProperty("timestamp").gt(weekAgoTimestamp))
                    .add(AuditEntity.revisionType().eq(RevisionType.MOD))
                    .addProjection(AuditEntity.property("isMarkedAsDeleted"));

            List<Object[]> results = (List<Object[]>) query.getResultList();
            for (Object result : results) {
                boolean isMarkedAsDeleted = (boolean) result;
                if (!isMarkedAsDeleted) {
                    shouldRemainAsOutdated = false;
                    break;
                }
            }

            if (shouldRemainAsOutdated) {
                remaining.add(hotelId);
            }
        }

        return remaining;
    }

    public List<Long> getIdsByName(String name) {
        if (name.length() < 3)
            return Collections.emptyList();

        String lowercaseName = StringUtils.lowerCase(name);
        return repository.getIdsByName(lowercaseName, false);
    }

    public List<Long> getIdsByCity(String city) {
        if (city.length() < 3)
            return Collections.emptyList();

        String lowercaseCity = StringUtils.lowerCase(city);
        List<Object[]> candidates = repository.getCandidatesByCity(lowercaseCity, false);

        List<Long> remainingIds = new ArrayList<>();
        for (Object[] candidate : candidates) {
            // extract id and address from the hashmap (which always contains one key-value pair)
            Long id = (Long) candidate[0];
            String address = (String) candidate[1];

            // assume that the city is the penultimate element in the 'address' field
            // each element is separated by a comma
            String elements[] = address.split(",");
            if (elements.length >= 2) {
                String extractedCity = elements[elements.length - 2].trim();
                String extractedCityLowercase = StringUtils.lowerCase(extractedCity);

                if (extractedCityLowercase.contains(lowercaseCity))
                    remainingIds.add(id);
            }
        }

        return remainingIds;
    }

    public List<Long> getIdsByNameAndCity(String name, String city) {
        if (name.length() < 3 || city.length() < 3)
            return Collections.emptyList();

        // for now simply find the intersection
        Set<Long> byName = new HashSet<>(getIdsByName(name));
        Set<Long> byCity = new HashSet<>(getIdsByCity(city));
        byName.retainAll(byCity);

        return new ArrayList<>(byName);
    }
}
