package com.horeca.site.services;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.hoteldata.HotelData;
import com.horeca.site.models.hoteldata.HotelDataView;
import com.horeca.site.repositories.hoteldata.HotelDataRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

// TODO the methods that are used to filter hotel data should implement an algorithm that measures the similarity
// between two words (e.g. Levenshtein distance)

@Service
@Transactional
public class HotelDataService {

    @Autowired
    private HotelDataRepository repository;

    public HotelDataView get(Long id) {
        HotelData hotelData = repository.findOne(id);
        if (hotelData == null)
            throw new ResourceNotFoundException();

        return hotelData.toView();
    }

    public Page<HotelDataView> getByName(String name, Pageable pageable) {
        String lowercaseName = StringUtils.lowerCase(name);
        List<HotelData> found = repository.getByName(lowercaseName);
        List<HotelDataView> views = new ArrayList<>();
        for (HotelData hotelData : found) {
            views.add(hotelData.toView());
        }

        return getPageForContent(views, pageable);
    }

    public Page<HotelDataView> getByCity(String city, Pageable pageable) {
        String lowercaseCity = StringUtils.lowerCase(city);
        List<HotelData> candidates = repository.getByCity(lowercaseCity);
        List<HotelDataView> foundViews = new ArrayList<>();
        for (HotelData hotelData : candidates) {
            // assume that the city is the penultimate element in the 'address' field
            // each element is separated by a comma
            String address = hotelData.getAddress();
            String elements[] = address.split(",");
            String extractedCity = elements[elements.length - 2].trim();
            String extractedCityLowercase = StringUtils.lowerCase(extractedCity);

            if (lowercaseCity.equals(extractedCityLowercase))
                foundViews.add(hotelData.toView());
        }

        return getPageForContent(foundViews, pageable);
    }

    public Page<HotelDataView> getByCity(String city) {
        return null;
    }

    public Page<HotelDataView> getBatch(Pageable pageable) {
        Iterable<HotelData> batch = repository.findAll(pageable);
        List<HotelDataView> views = new ArrayList<>();
        for (HotelData hotelData : batch) {
            views.add(hotelData.toView());
        }

        PageImpl<HotelDataView> result = new PageImpl<HotelDataView>(views, pageable, repository.getTotalCount());
        return result;
    }

    private static <T> Page<T> getPageForContent(List<T> content, Pageable pageable) {
        int totalCount = content.size();
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        int fromIndex = Math.max(pageNumber * pageSize, 0);
        int toIndex = Math.max(Math.min(fromIndex + pageSize, totalCount), 0);

        List<T> result = new ArrayList<>();
        if (fromIndex < totalCount)
            result = content.subList(fromIndex, toIndex);

        return new PageImpl<>(result, pageable, totalCount);
    }
}
