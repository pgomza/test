package com.horeca.site.services;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.extractors.HotelDataExtractor;
import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.hotel.HotelView;
import com.horeca.site.models.hotel.images.FileLink;
import com.horeca.site.models.hotel.information.UsefulInformation;
import com.horeca.site.models.hotel.information.UsefulInformationHourItem;
import com.horeca.site.repositories.HotelRepository;
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
import java.util.stream.Collectors;

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

    @Autowired
    private HotelSearchService hotelSearchService;

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
        // TODO needs refactoring
        if (hotel.getImages() == null)
            hotel.setImages(new ArrayList<>());
        repository.save(hotel);
        ensureEnoughInfoAboutHotel(hotel.getId());
        return get(hotel.getId());
    }

    public Iterable<Hotel> addAll(Iterable<Hotel> hotels) {
        return repository.save(hotels); // don't return a populated List due to performance reasons
    }

    public Hotel update(Long id, Hotel updated) {
        updated.setId(id); // TODO this should have been set by the time this method is invoked
        checkImagesOnUpdate(updated);
        return repository.save(updated);
    }

    public Hotel updateIgnoringSomeFields(Long id, Hotel newOne) {
        Hotel current = get(id);
        // don't let this update overwrite info about the guests - ignore whatever has been set in newOne as 'guests'
        // there's a different endpoint specifically intended for managing the guests
        newOne.setGuests(current.getGuests());
        // the same applies for the notification settings
        newOne.setNotificationSettings(current.getNotificationSettings());

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
                hotelImagesService.save(hotelId, HotelImagesService.DEFAULT_FILENAME, new FileInputStream(file));
            } catch (FileNotFoundException e) {
                throw new RuntimeException("There was a problem while trying to set the default image for " +
                        "hotel " + hotelId, e);
            }
        }

        update(hotelId, hotel);
    }

    /**
     * Checks what (if anything) has been changed in the hotel's images
     * Only a change in their order is allowed
     */
    private void checkImagesOnUpdate(Hotel updatedHotel) {
        Hotel currentHotel = get(updatedHotel.getId());
        Set<FileLink> currentImages = new HashSet<>(currentHotel.getImages());
        Set<FileLink> updatedImages = null;

        boolean failedRetrieval = false;
        try {
            updatedImages = updatedHotel.getImages().stream()
                    .map(image -> hotelImagesService.get(updatedHotel.getId(), image.getFilename()))
                    .collect(Collectors.toSet());
        }
        catch (ResourceNotFoundException ex) {
            failedRetrieval = true;
        }

        if (!failedRetrieval && currentImages.size() == updatedImages.size()) {
            currentImages.retainAll(updatedImages);
            if (currentImages.size() == updatedImages.size())
                return;
        }
        throw new BusinessRuleViolationException("Only a change in the order of the existing hotel images is allowed. " +
                "To add or delete an image use the ~/api/hotels/" + updatedHotel.getId() + "/images endpoint");
    }

    /*
        filtering hotels
     */

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
