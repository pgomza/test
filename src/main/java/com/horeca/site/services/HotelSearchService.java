package com.horeca.site.services;

import com.horeca.site.repositories.HotelRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HotelSearchService {

    @Autowired
    private HotelRepository repository;

    public List<Long> getIdsByName(String name) {
        if (name.length() < 3)
            return Collections.emptyList();

        String lowercaseName = StringUtils.lowerCase(name);
        return repository.getIdsByName(lowercaseName);
    }

    public List<Long> getIdsByCity(String city) {
        if (city.length() < 3)
            return Collections.emptyList();

        String lowercaseCity = StringUtils.lowerCase(city);
        List<Object[]> candidates = repository.getCandidatesByCity(lowercaseCity);

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
