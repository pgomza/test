package com.horeca.site.services;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.extractors.HotelDataExtractor;
import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.hotel.HotelView;
import com.horeca.site.models.hotel.information.UsefulInformation;
import com.horeca.site.models.hotel.information.UsefulInformationHourItem;
import com.horeca.site.repositories.HotelRepository;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

@Service
@Transactional
public class HotelService {

    @Autowired
    private HotelRepository repository;

    // TODO avoid a circular dependency: HotelImagesService is dependent on HotelService
    // for now, @Lazy is used as a workaround
    @Autowired
    @Lazy
    private HotelImagesService hotelImagesService;

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

    @PreAuthorize("hasRole('SALESMAN')")
    public Hotel add(Hotel hotel) {
        return repository.save(hotel);
    }

    public Iterable<Hotel> addAll(Iterable<Hotel> hotels) {
        return repository.save(hotels); // don't return a populated List due to performance reasons
    }

    public Hotel update(Long id, Hotel newOne) {
        newOne.setId(id); // TODO this should have been set by the time this method is invoked
        return repository.save(newOne);
    }

    public Hotel updateIgnoreGuests(Long id, Hotel newOne) {
        Hotel current = get(id);
        // don't let this update overwrite info about the guests - ignore whatever has been set in newOne as 'guests'
        // there's a different endpoint specifically intended for managing the guests
        newOne.setGuests(current.getGuests());

        return update(id, newOne);
    }

    public void delete(Long id) {
        Hotel toDelete = get(id);
        repository.delete(toDelete);
    }

    public void ensureExists(Long hotelId) {
        boolean exists = repository.exists(hotelId);
        if (!exists)
            throw new ResourceNotFoundException("Could not find a hotel with such an id");
    }

    /**
     * Checks whether the hotel contains enough information for clients
     * to process it properly; if not, it adds default data
     * This method doesn't attempt to add such crucial data as the hotel's
     * name in case it hasn't been specified
     */
    public void ensureEnoughInfoAboutHotel(Long hotelId) {
        Hotel hotel = get(hotelId);
        if (hotel.getDescription() == null)
            hotel.setDescription("");

        if (hotel.getStarRating() == null)
            hotel.setStarRating(0F);

        if (hotel.getPropertyType() == null)
            hotel.setPropertyType("Hotel");

        if (hotel.getUsefulInformation() == null) {
            UsefulInformation usefulInformation = new UsefulInformation();

            UsefulInformationHourItem checkOut = new UsefulInformationHourItem();
            checkOut.setName("Check-out");
            checkOut.setFromHour(LocalTime.parse("08:00"));
            checkOut.setToHour(LocalTime.parse("18:00"));

            Set<UsefulInformationHourItem> hours = new HashSet<>();
            hours.add(checkOut);
            usefulInformation.setHours(hours);
            hotel.setUsefulInformation(usefulInformation);
        }

        if (hotel.getImages() == null || hotel.getImages().size() == 0) {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("other/defaultHotelImage.png").getFile());
            try {
                hotelImagesService.save(hotelId, hotelImagesService.DEFAULT_FILENAME, new FileInputStream(file));
            } catch (FileNotFoundException e) {
                throw new RuntimeException("There was a problem while trying to set the default image for " +
                        "hotel " + hotelId, e);
            }
        }

        update(hotelId, hotel);
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
