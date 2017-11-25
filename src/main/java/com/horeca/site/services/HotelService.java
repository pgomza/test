package com.horeca.site.services;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.Currency;
import com.horeca.site.models.cubilis.CubilisConnectionStatus;
import com.horeca.site.models.cubilis.CubilisSettings;
import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.hotel.images.FileLink;
import com.horeca.site.models.hotel.information.UsefulInformation;
import com.horeca.site.models.hotel.information.UsefulInformationHourItem;
import com.horeca.site.models.notifications.NotificationSettings;
import com.horeca.site.repositories.HotelRepository;
import com.horeca.site.security.services.GuestAccountService;
import com.horeca.site.security.services.UserAccountService;
import com.horeca.site.services.services.StayService;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
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

    @Autowired
    private HotelQueryService hotelQueryService;

    // TODO avoid a circular dependency: HotelImagesService is dependent on HotelService
    // for now, @Lazy is used as a workaround
    @Autowired
    @Lazy
    private HotelImagesService hotelImagesService;

    @Autowired
    @Lazy
    private StayService stayService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private GuestAccountService guestAccountService;

    /*
        Expose these two methods in this service to prevent clients
        from autowiring the query service just to use them; the 'get' method
        is by far the most popular one
     */
    public Hotel get(Long id) {
        return hotelQueryService.get(id);
    }

    public Page<Hotel> getAll(Pageable pageable) {
        return hotelQueryService.getAll(pageable);
    }


    @PreAuthorize("hasRole('SALESMAN')")
    public Hotel add(Hotel hotel) {
        // TODO needs refactoring
        if (hotel.getImages() == null)
            hotel.setImages(new ArrayList<>());

        hotel.setIsThrodiPartner(false);
        hotel.setIsMarkedAsDeleted(false);

        if (hotel.getCurrency() == null)
            hotel.setCurrency(Currency.EURO);

        repository.save(hotel);
        ensureEnoughInfoAboutHotel(hotel.getId());

        return hotelQueryService.get(hotel.getId());
    }

    public Iterable<Hotel> addAll(Iterable<Hotel> hotels) {
        return repository.save(hotels); // don't return a populated List due to performance reasons
    }

    public Hotel update(Long id, Hotel updated) {
        updated.setId(id); // TODO this should have been set by the time this method is invoked
        checkImagesOnUpdate(updated);
        return repository.save(updated);
    }

    public Hotel updateFromController(Long id, Hotel newOne) {
        Hotel current = hotelQueryService.get(id);
        // don't let this update overwrite info about the guests - ignore whatever has been set in newOne as 'guests'
        // there's a different endpoint specifically intended for managing the guests
        newOne.setGuests(current.getGuests());
        // the same applies for a few other fields
        newOne.setNotificationSettings(current.getNotificationSettings());
        newOne.setCubilisSettings(current.getCubilisSettings());
        newOne.setCubilisConnectionStatus(current.getCubilisConnectionStatus());
        newOne.setIsMarkedAsDeleted(current.getIsMarkedAsDeleted());

        return update(id, newOne);
    }

    public List<String> updateTVChannels(Long id, List<String> updated) {
        Hotel hotel = hotelQueryService.get(id);
        hotel.setTvChannels(updated);
        update(id, hotel);
        return hotel.getTvChannels();
    }

    public void reset(Long id) {
        Hotel hotel = hotelQueryService.get(id);
        if (!hotel.getIsTestHotel()) {
            throw new BusinessRuleViolationException("You mustn't reset a non-test hotel");
        }

        Collection<String> pinsToDelete = stayService.getByHotelId(id);
        pinsToDelete.forEach(pin -> stayService.delete(pin));

        hotel.setIsTestHotel(true);
        hotel.setCurrency(Currency.EURO);
        hotel.setDescription(null);
        hotel.setEmail(null);
        hotel.setWebsite(null);
        hotel.setPhone(null);
        hotel.setBookingUrl(null);
        hotel.setShopsUrl(null);
        hotel.setRestaurantsUrl(null);
        hotel.setInterestingPlacesUrl(null);
        hotel.setEventsUrl(null);
        hotel.setFax(null);
        hotel.setStarRating(null);
        hotel.setRooms(null);
        hotel.setRatingOverall(null);
        hotel.setRatingOverallText(null);
        hotel.setPropertyType(null);
        hotel.setChain(null);
        hotel.setLongitude(null);
        hotel.setLatitude(null);
        hotel.setUsefulInformation(null);
        hotel.setRoomDirectory(null);
        hotel.setAvailableServices(null);

        if (hotel.getImages() != null) {
            List<String> imageFilenames = hotel.getImages().stream()
                    .map(FileLink::getFilename)
                    .collect(Collectors.toList());

            imageFilenames.forEach(filename -> hotelImagesService.delete(hotel.getId(), filename));
            hotel.getImages().clear();
        }

        if (hotel.getGuests() != null) {
            hotel.getGuests().clear();
        }

        hotel.setNotificationSettings(null);

        update(id, hotel);
        ensureEnoughInfoAboutHotel(id);
    }

    @PreAuthorize("hasRole('SALESMAN')")
    public void markAsDeleted(Long id) {
        Hotel hotel = hotelQueryService.get(id);
        hotel.setIsMarkedAsDeleted(true);

        userAccountService.disableAllInHotel(id);
        Collection<String> staysInHotel = stayService.getByHotelId(id);
        staysInHotel.forEach(guestAccountService::disableForStay);

        update(id, hotel);
    }

    @PreAuthorize("hasRole('SALESMAN')")
    public void restore(Long id) {
        Hotel hotel = hotelQueryService.get(id);
        hotel.setIsMarkedAsDeleted(false);

        userAccountService.enableAllInHotel(id);
        Collection<String> staysInHotel = stayService.getByHotelId(id);
        staysInHotel.forEach(guestAccountService::enableForStay);

        update(id, hotel);
    }

    public void delete(Long id) {
        hotelQueryService.get(id);

        // delete all accounts and stays associated with this hotel
        userAccountService.deleteAllInHotel(id);
        Collection<String> associatedStays = stayService.getByHotelId(id);
        associatedStays.forEach(pin -> {
            stayService.delete(pin);
            guestAccountService.deleteForStay(pin);
        });



        repository.delete(id);
    }

    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000)
    public void deleteMarkedAndOutdated() {
        List<Long> markedAsDeleted = hotelQueryService.getMarkedAndOutdated();
        markedAsDeleted.forEach(this::delete);
    }

    /**
     * Checks whether the hotel contains enough information for clients
     * to process it properly; if not, it adds default data
     * This method doesn't attempt to add such crucial data as the hotel's
     * name in case it hasn't been specified
     */
    public void ensureEnoughInfoAboutHotel(Long hotelId) {
        Hotel hotel = hotelQueryService.get(hotelId);
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
            checkOut.setFromHour(LocalTime.parse("18:00"));
            checkOut.setToHour(LocalTime.parse("18:00"));

            Set<UsefulInformationHourItem> hours = new HashSet<>();
            hours.add(checkOut);
            usefulInformation.setHours(hours);
            hotel.setUsefulInformation(usefulInformation);
        }

        if (hotel.getImages() == null)
            hotel.setImages(new ArrayList<>());

        if (hotel.getImages().size() == 0) {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("other/defaultHotelImage.png").getFile());
            try {
                hotelImagesService.save(hotelId, HotelImagesService.DEFAULT_FILENAME, new FileInputStream(file));
            } catch (FileNotFoundException e) {
                throw new RuntimeException("There was a problem while trying to set the default image for " +
                        "hotel " + hotelId, e);
            }
        }

        if (hotel.getNotificationSettings() == null) {
            NotificationSettings settings = new NotificationSettings();
            settings.setEmail("");
            hotel.setNotificationSettings(settings);
        }

        if (hotel.getCubilisSettings() == null) {
            CubilisSettings settings = new CubilisSettings();
            settings.setEnabled(false);
            settings.setLogin("someone@example.com");
            settings.setPassword("pass123");
            hotel.setCubilisSettings(settings);
        }

        if (hotel.getCubilisConnectionStatus() == null) {
            CubilisConnectionStatus connectionStatus = new CubilisConnectionStatus();
            connectionStatus.setStatus(CubilisConnectionStatus.Status.DISABLED);
            hotel.setCubilisConnectionStatus(connectionStatus);
        }

        update(hotelId, hotel);
    }

    /**
     * Checks what (if anything) has been changed in the hotel's images
     * Only a change in their order is allowed
     */
    private void checkImagesOnUpdate(Hotel updatedHotel) {
        Hotel currentHotel = hotelQueryService.get(updatedHotel.getId());
        if (currentHotel.getImages() != null) {
            Set<FileLink> currentImages = new HashSet<>(currentHotel.getImages());
            Set<FileLink> updatedImages = null;

            boolean failedRetrieval = false;
            try {
                updatedImages = updatedHotel.getImages().stream()
                        .map(image -> hotelImagesService.get(updatedHotel.getId(), image.getFilename()))
                        .collect(Collectors.toSet());
            } catch (ResourceNotFoundException ex) {
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
    }
}
