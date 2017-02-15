package com.horeca.site.services;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.hoteldata.HotelData;
import com.horeca.site.models.hoteldata.HotelDataView;
import com.horeca.site.repositories.hoteldata.HotelDataRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public HotelDataView getByName(String name) {
        // TODO implement the actual functionality; for now return a random Hotel from the first 100
        Page<HotelData> firstPage = repository.findAll(new PageRequest(0, 100));
        List<HotelData> batch = new ArrayList<>(firstPage.getContent());
        Collections.shuffle(batch);
        return batch.get(0).toView();
    }

    public Page<HotelDataView> getByCity(String city, Pageable pageable) {
        String lowercaseCity = StringUtils.lowerCase(city);
        List<HotelData> candidates = repository.getByCity(lowercaseCity);
        List<HotelDataView> found = new ArrayList<>();
        for (HotelData hotelData : candidates) {
            // assume that the city is the penultimate element in the 'address' field
            // each element is separated by a comma
            String address = hotelData.getAddress();
            String elements[] = address.split(",");
            String extractedCity = elements[elements.length - 2].trim();
            String extractedCityLowercase = StringUtils.lowerCase(extractedCity);

            // for now simply compare both strings
            // TODO implement an algorithm that measures the similarity between two words (e.g. Levenshtein distance)
            if (lowercaseCity.equals(extractedCityLowercase))
                found.add(hotelData.toView());
        }

        // prepare the response
        int totalCount = found.size();
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        int fromIndex = Math.max(pageNumber * pageSize, 0);
        int toIndex = Math.max(Math.min(fromIndex + pageSize, totalCount), 0);

        List<HotelDataView> result = new ArrayList<>();
        if (fromIndex < totalCount)
            result = found.subList(fromIndex, toIndex);

        return new PageImpl<>(result, pageable, totalCount);
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
}
