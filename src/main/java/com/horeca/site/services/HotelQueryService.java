package com.horeca.site.services;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.hotel.HotelView;
import com.horeca.site.repositories.HotelRepository;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class HotelQueryService {

    @Autowired
    private HotelRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private HotelSearchService hotelSearchService;

    /*
        filtering/retrieval
     */

    public Hotel get(Long id) {
        Hotel hotel = repository.findOne(id);
        if (hotel == null)
            throw new ResourceNotFoundException();

        return hotel;
    }

    public List<String> getTVChannels(Long id) {
        return get(id).getTvChannels();
    }


    // The following get* methods exclude the hotels that have been marked as deleted
    // The methods aren't to be used from anywhere else than the controllers (for most of them
    // it's obvious because they receive an argument of the Pageable type)

    public Hotel getIfNotMarkedAsDeleted(Long id) {
        Hotel hotel = get(id);
        if (hotel.getIsMarkedAsDeleted()) {
            throw new ResourceNotFoundException();
        }

        return hotel;
    }

    public HotelView getViewIfNotMarkedAsDeleted(Long id) {
        return getIfNotMarkedAsDeleted(id).toView();
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

    public Page<Hotel> getAll(Pageable pageable) {
        List<Long> foundIds = hotelSearchService.getAll();
        return getPageOfHotels(foundIds, pageable);
    }

    public Page<HotelView> getAllViews(Pageable pageable) {
        List<Long> foundIds = hotelSearchService.getAll();
        return getPageOfHotelViews(foundIds, pageable);
    }

    public Page<Hotel> getByName(String name, Pageable pageable) {
        List<Long> foundIds = hotelSearchService.getIdsByName(name);
        return getPageOfHotels(foundIds, pageable);
    }

    public Page<HotelView> getViewsByName(String name, Pageable pageable) {
        List<Long> foundIds = hotelSearchService.getIdsByName(name);
        return getPageOfHotelViews(foundIds, pageable);
    }

    public Page<Hotel> getByCity(String city, Pageable pageable) {
        List<Long> foundIds = hotelSearchService.getIdsByCity(city);
        return getPageOfHotels(foundIds, pageable);
    }

    public Page<HotelView> getViewsByCity(String city, Pageable pageable) {
        List<Long> foundIds = hotelSearchService.getIdsByCity(city);
        return getPageOfHotelViews(foundIds, pageable);
    }

    public Page<Hotel> getByNameAndCity(String name, String city, Pageable pageable) {
        if (name.isEmpty() && city.isEmpty())
            return getEmptyPage(pageable);
        else if (name.isEmpty())
            return getByCity(city, pageable);
        else if (city.isEmpty())
            return getByName(name, pageable);
        else {
            List<Long> foundIds = hotelSearchService.getIdsByNameAndCity(name, city);
            return getPageOfHotels(foundIds, pageable);
        }
    }

    public Page<HotelView> getViewsByNameAndCity(String name, String city, Pageable pageable) {
        if (name.isEmpty() && city.isEmpty())
            return getEmptyPage(pageable);
        else if (name.isEmpty())
            return getViewsByCity(city, pageable);
        else if (city.isEmpty())
            return getViewsByName(name, pageable);
        else {
            List<Long> foundIds = hotelSearchService.getIdsByNameAndCity(name, city);
            return getPageOfHotelViews(foundIds, pageable);
        }
    }

    private Page<Hotel> getPageOfHotels(List<Long> hotelIds, Pageable pageable) {
        List<Hotel> hotels = fetchBatchAndSort(hotelIds, pageable);
        return getPageForContent(hotels, pageable, hotelIds.size());
    }

    private Page<HotelView> getPageOfHotelViews(List<Long> hotelIds, Pageable pageable) {
        List<Hotel> hotels = fetchBatchAndSort(hotelIds, pageable);
        List<HotelView> views = hotels.stream().map(hotel -> hotel.toView()).collect(Collectors.toList());
        return getPageForContent(views, pageable, hotelIds.size());
    }

    private static <T> Page<T> getEmptyPage(Pageable pageable) {
        return new PageImpl<>(new ArrayList<>(), pageable, 0L);
    }

    private static <T> Page<T> getPageForContent(List<T> content, Pageable pageable, int totalCount) {
        return new PageImpl<>(content, pageable, totalCount);
    }

    private List<Hotel> fetchBatchAndSort(List<Long> hotelIds, Pageable pageable) {
        int totalCount = hotelIds.size();
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        int fromIndex = Math.max(pageNumber * pageSize, 0);
        int toIndex = Math.max(Math.min(fromIndex + pageSize, totalCount), 0);

        List<Long> idsToFetch = hotelIds.subList(fromIndex, toIndex);
        List<Hotel> fetched = idsToFetch.stream().map(id -> repository.findOne(id)).collect(Collectors.toList());
        Collections.sort(fetched, new HotelComparator());
        return fetched;
    }

    private static class HotelComparator implements Comparator<Hotel> {
        @Override
        public int compare(Hotel hotel1, Hotel hotel2) {
            if (hotel1.getIsThrodiPartner() && (!hotel2.getIsThrodiPartner()))
                return -1;
            else if ((!hotel1.getIsThrodiPartner()) && hotel2.getIsThrodiPartner())
                return 1;
            else { // simply sort by id
                if (hotel1.getId() < hotel2.getId())
                    return -1;
                else if (hotel1.getId() > hotel2.getId())
                    return 1;
                else
                    return 0;
            }
        }
    }

    /*
        Cubilis-related functionality
     */

    public List<Long> getIdsOfCubilisEligible() {
        return repository.getIdsOfCubilisEligible();
    }
}
