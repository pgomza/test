package com.horeca.site.services;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.extractors.HotelDataExtractor;
import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.hotel.HotelView;
import com.horeca.site.repositories.HotelRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class HotelService {

    @Autowired
    private HotelRepository repository;

    /*
        general actions
     */

    public Page<Hotel> getAll(Pageable pageable) {
        Iterable<Hotel> batchIterable = repository.findAll(pageable);
        List<Hotel> batchList = new ArrayList<>();
        for (Hotel hotel : batchIterable) {
            batchList.add(hotel);
        }
        return new PageImpl<>(batchList, pageable, repository.getTotalCount());
    }

    public Page<HotelView> getAllViews(Pageable pageable) {
        Iterable<Hotel> batch = repository.findAll(pageable);
        List<HotelView> views = new ArrayList<>();
        for (Hotel hotel : batch) {
            views.add(hotel.toView());
        }

        PageImpl<HotelView> result = new PageImpl<>(views, pageable, repository.getTotalCount());
        return result;
    }

    public Hotel get(Long id) {
        Hotel hotel = repository.findOne(id);
        if (hotel == null)
            throw new ResourceNotFoundException();

        return hotel;
    }

    public HotelView getView(Long id) {
        Hotel hotel = get(id);
        return hotel.toView();
    }

    public Hotel add(Hotel hotel) {
        return repository.save(hotel);
    }

    public Iterable<Hotel> addAll(Iterable<Hotel> hotels) {
        return repository.save(hotels); // don't return a populated List due to performance reasons
    }

    public Hotel update(Long id, Hotel newOne) {
        newOne.setId(id);
        Hotel changed = repository.save(newOne);

        return changed;
    }

    public void delete(Long id) {
        Hotel toDelete = get(id);
        repository.delete(toDelete);
    }

    /*
        filtering hotels
        // TODO refactor the filtering methods because as of now they're doing pretty much the same thing
     */

    private List<Hotel> filterByName(String name) {
        if (name.length() < 3)
            return Collections.emptyList();

        String lowercaseName = StringUtils.lowerCase(name);
        List<Hotel> found = repository.getByName(lowercaseName);
        Collections.sort(found, new HotelComparator());
        return found;
    }

    private List<Hotel> filterByCity(String city) {
        if (city.length() < 3)
            return Collections.emptyList();

        String lowercaseCity = StringUtils.lowerCase(city);
        List<Hotel> found = repository.getByCity(lowercaseCity);
        Collections.sort(found, new HotelComparator());
        return found;
    }

    private List<Hotel> filterByNameAndCity(String name, String city) {
        if (name.length() < 3 || city.length() < 3)
            return Collections.emptyList();

        // for now simply find the intersection
        List<Hotel> byName = filterByName(name);
        List<Hotel> byCity = filterByCity(city);
        byName.retainAll(byCity);
        Collections.sort(byName, new HotelComparator());

        return byName;
    }

    public Page<Hotel> getByName(String name, Pageable pageable) {
        List<Hotel> found = filterByName(name);
        return getPageForContent(found, pageable);
    }

    public Page<HotelView> getViewsByName(String name, Pageable pageable) {
        List<Hotel> found = filterByName(name);
        List<HotelView> views = new ArrayList<>();
        for (Hotel hotel : found) {
            views.add(hotel.toView());
        }

        return getPageForContent(views, pageable);
    }

    public Page<Hotel> getByCity(String city, Pageable pageable) {
        List<Hotel> found = filterByCity(city);
        return getPageForContent(found, pageable);
    }

    public Page<HotelView> getViewsByCity(String city, Pageable pageable) {
        List<Hotel> found = filterByCity(city);
        List<HotelView> views = new ArrayList<>();
        for (Hotel hotel : found) {
            views.add(hotel.toView());
        }
        return getPageForContent(views, pageable);
    }

    public Page<Hotel> getByNameAndCity(String name, String city, Pageable pageable) {
        if (name.isEmpty() && city.isEmpty())
            return getEmptyPage(pageable);
        else if (name.isEmpty())
            return getByCity(city, pageable);
        else if (city.isEmpty())
            return getByName(name, pageable);
        else {
            List<Hotel> found = filterByNameAndCity(name, city);
            return getPageForContent(found, pageable);
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
            List<Hotel> found = filterByNameAndCity(name, city);
            List<HotelView> views = new ArrayList<>();
            for (Hotel hotel : found) {
                views.add(hotel.toView());
            }

            return getPageForContent(views, pageable);
        }
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

    private static <T> Page<T> getEmptyPage(Pageable pageable) {
        return new PageImpl<>(new ArrayList<T>(), pageable, 0L);
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
        conversion
     */

    public Hotel convertFromHotelData(HotelDataExtractor.HotelData hotelData) {
        Hotel hotel = new Hotel();
        hotel.setName(hotelData.title);
        hotel.setDescription(hotelData.description);
        hotel.setAddress(hotelData.addressFull);
        hotel.setEmail(hotelData.email);
        hotel.setWebsite(hotelData.website);
        hotel.setPhone(hotelData.phone);
        hotel.setFax(hotelData.fax);
        hotel.setStarRating(hotelData.starRating);
        hotel.setRooms(hotelData.rooms);
        hotel.setRatingOverall(hotelData.ratingOverall);
        hotel.setRatingOverallText(hotelData.ratingOverallText);
        hotel.setPropertyType(hotelData.propertyType);
        hotel.setChain(hotelData.chain);
        hotel.setLongitude(hotelData.longitude);
        hotel.setLatitude(hotelData.latitude);
        return hotel;
    }
}
