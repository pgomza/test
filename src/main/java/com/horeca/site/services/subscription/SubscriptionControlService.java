package com.horeca.site.services.subscription;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.models.cubilis.CubilisSettings;
import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.hotel.services.AvailableServices;
import com.horeca.site.models.hotel.subscription.SubscriptionLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SubscriptionControlService {

    @Autowired
    private SubscriptionService subscriptionService;

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
                throw new BusinessRuleViolationException("The free subscription forbids setting the PMS to 'enabled'");
            }
        }
    }

    private void throwExceptionForService(String serviceName) {
        throw new BusinessRuleViolationException(
                "The free subscription forbids setting the " + serviceName + " service to 'available'"
        );
    }
}
