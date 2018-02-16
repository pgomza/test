package com.horeca.site.services.subscription;

import com.horeca.site.models.cubilis.CubilisSettings;
import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.hotel.services.AvailableServices;
import com.horeca.site.models.hotel.subscription.Subscription;
import com.horeca.site.models.hotel.subscription.SubscriptionEvent;
import com.horeca.site.models.hotel.subscription.SubscriptionLevel;
import com.horeca.site.models.hotel.subscription.SubscriptionScheduling;
import com.horeca.site.repositories.SubscriptionSchedulingRepository;
import com.horeca.site.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class SubscriptionControlService {

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private SubscriptionSchedulingRepository subscriptionSchedulingRepository;

    @Autowired
    private HotelService hotelService;

    public void ensureHotelCanBeUpdated(Long hotelId, Hotel targetVersion) {
        int currentLevel = subscriptionService.getCurrentLevel(hotelId);
        ensureHotelCanBeUpdated(currentLevel, targetVersion);
    }

    public void ensureHotelCanBeUpdated(int currentLevel, Hotel targetVersion) {
        if (currentLevel == SubscriptionLevel.BASIC.getNumber()) {
            AvailableServices targetServices = targetVersion.getAvailableServices();
            if (targetServices != null) {
                // the free subscription permits updating availability of the Housekeeping service only
                if (targetServices.getBreakfast().getAvailable()) {
                    throwExceptionForService("Breakfast");
                }
                if (targetServices.getCarPark().getAvailable()) {
                    throwExceptionForService("Car Park");
                }
                if (targetServices.getSpa().getAvailable()) {
                    throwExceptionForService("Spa");
                }
                if (targetServices.getPetCare().getAvailable()) {
                    throwExceptionForService("Pet Care");
                }
                if (targetServices.getTaxi().getAvailable()) {
                    throwExceptionForService("Taxi");
                }
                if (targetServices.getRoomService().getAvailable()) {
                    throwExceptionForService("Room Service");
                }
                if (targetServices.getTableOrdering().getAvailable()) {
                    throwExceptionForService("Table Ordering");
                }
                if (targetServices.getBar().getAvailable()) {
                    throwExceptionForService("Bar");
                }
                if (targetServices.getSpaCall().getAvailable()) {
                    throwExceptionForService("Spa Call");
                }
                if (targetServices.getHairDresser().getAvailable()) {
                    throwExceptionForService("Hairdresser");
                }
                if (targetServices.getRental().getAvailable()) {
                    throwExceptionForService("Rental");
                }
                if (targetServices.getRestaurantMenu().getAvailable()) {
                    throwExceptionForService("Restaurant Menu");
                }
            }

            CubilisSettings cubilisSettings = targetVersion.getCubilisSettings();
            if (cubilisSettings.isEnabled()) {
                throwExceptionForCubilis();
            }
        }
    }

    public void ensureCubilisSettingsCanBeUpdated(Long hotelId, CubilisSettings targetSettings) {
        int currentLevel = subscriptionService.getCurrentLevel(hotelId);
        if (currentLevel == SubscriptionLevel.BASIC.getNumber()) {
            if (targetSettings.isEnabled()) {
                throwExceptionForCubilis();
            }
        }
    }

    private void throwExceptionForService(String serviceName) {
        throw new AccessDeniedException(
                "The free subscription forbids setting the " + serviceName + " service to 'available'"
        );
    }

    private void throwExceptionForCubilis() {
        throw new AccessDeniedException("The free subscription forbids setting the PMS to 'enabled'");
    }

    @Scheduled(fixedDelay = 5 * 60 * 1000)
    public void checkSubscriptionsValidity() {
        SubscriptionScheduling schedulingInfo = subscriptionSchedulingRepository.findOne(1L);
        Timestamp lastTimestampChecked = schedulingInfo.getLastTimestampChecked();
        Set<Subscription> withNewerEventsThanNow = subscriptionService.getWithEventsNewerThan(lastTimestampChecked);

        Timestamp now = new Timestamp(System.currentTimeMillis());
        for (Subscription subscription : withNewerEventsThanNow) {
            List<SubscriptionEvent> history = subscription.getHistory();
            if (!history.isEmpty()) {
                // for the time being ignore the subscription levels
                SubscriptionEvent lastEvent = history.get(history.size() - 1);
                if (lastEvent.getExpiresAt().before(now)) {
                    disableServicesInHotel(subscription.getHotel().getId());
                    schedulingInfo.setLastTimestampChecked(now);
                    subscriptionSchedulingRepository.save(schedulingInfo);
                }
            }
        }


    }

    private void disableServicesInHotel(Long hotelId) {
        Hotel hotel = hotelService.get(hotelId);

        AvailableServices services = hotel.getAvailableServices();
        services.getBreakfast().setAvailable(false);
        services.getCarPark().setAvailable(false);
        services.getSpa().setAvailable(false);
        services.getPetCare().setAvailable(false);
        services.getTaxi().setAvailable(false);
        services.getRoomService().setAvailable(false);
        services.getTableOrdering().setAvailable(false);
        services.getBar().setAvailable(false);
        services.getSpaCall().setAvailable(false);
        services.getHairDresser().setAvailable(false);
        services.getRental().setAvailable(false);
        services.getRestaurantMenu().setAvailable(false);

        hotel.getCubilisSettings().setEnabled(false);

        hotelService.update(hotelId, hotel);
    }
}
