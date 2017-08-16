package com.horeca.site.services.cubilis;

import com.horeca.site.exceptions.UnauthorizedException;
import com.horeca.site.models.cubilis.CubilisConnectionStatus;
import com.horeca.site.models.cubilis.CubilisReservation;
import com.horeca.site.models.cubilis.CubilisRoomsPerHotel;
import com.horeca.site.models.cubilis.CubilisSettings;
import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.updates.ChangeInHotelEvent;
import com.horeca.site.repositories.cubilis.CubilisConnectionStatusRepository;
import com.horeca.site.repositories.cubilis.CubilisSettingsRepository;
import com.horeca.site.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class CubilisService {

    @Autowired
    private CubilisConnectorService connectorService;

    @Autowired
    private CubilisReservationService reservationService;

    @Autowired
    private CubilisSettingsRepository settingsRepository;

    @Autowired
    private CubilisConnectionStatusRepository connectionStatusRepository;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public CubilisSettings getSettings(Long hotelId) {
        Hotel hotel = hotelService.get(hotelId);
        return hotel.getCubilisSettings();
    }

    public CubilisSettings updateSettings(Long hotelId, CubilisSettings updated) {
        CubilisSettings current = getSettings(hotelId);
        updated.setId(current.getId());
        CubilisSettings savedSettings = settingsRepository.save(updated);

        updateConnectionStatus(hotelId);

        return savedSettings;
    }

    public CubilisConnectionStatus getConnectionStatus(Long hotelId) {
        Hotel hotel = hotelService.get(hotelId);
        return hotel.getCubilisConnectionStatus();
    }

    @Transactional(timeout = 20) // seconds
    CubilisConnectionStatus updateConnectionStatus(Long hoteldId) {
        CubilisSettings settings = getSettings(hoteldId);
        CubilisConnectionStatus currentStatus = getConnectionStatus(hoteldId);

        if (!settings.isEnabled()) {
            currentStatus.setStatus(CubilisConnectionStatus.Status.DISABLED);
        }
        else {
            CubilisConnectionStatus.Status status =
                    connectorService.checkConnectionStatus(settings.getLogin(), settings.getPassword());
            currentStatus.setStatus(status);
        }

        return connectionStatusRepository.save(currentStatus);
    }

    public List<CubilisRoomsPerHotel> getAvailableRooms(Long hotelId) {
        CubilisSettings settings = getSettings(hotelId);
        try {
            return connectorService.fetchAvailableRooms(settings.getLogin(), settings.getPassword());
        } catch (UnauthorizedException ex) {
            updateConnectionStatus(hotelId);
            throw ex;
        }
    }

    @Scheduled(fixedDelay = 60 * 1000)
    public void fetchAndUpdateReservations() {
        List<Long> hotelIds = hotelService.getIdsOfCubilisEligible();
        Map<Long, CubilisSettings> hotelIdToSettings = hotelIds.stream()
                .collect(Collectors.toMap(Function.identity(), this::getSettings));

        for (Map.Entry<Long, CubilisSettings> entry : hotelIdToSettings.entrySet()) {
            Long hotelId = entry.getKey();
            CubilisSettings settings = entry.getValue();

            try {
                List<CubilisReservation> fetchedReservations = connectorService.fetchReservations(settings.getLogin(), settings.getPassword());
                List<CubilisReservation> filteredReservations = filterFetchedReservations(fetchedReservations);

                if (!filteredReservations.isEmpty()) {
                    setHotelForReservations(hotelId, filteredReservations);

                    reservationService.save(filteredReservations);

                    eventPublisher.publishEvent(new ChangeInHotelEvent(this, hotelId));
                }
            } catch (UnauthorizedException ex) {
                updateConnectionStatus(hotelId);
            }
        }
    }

    private List<CubilisReservation> filterFetchedReservations(List<CubilisReservation> reservations) {
        return reservations.stream()
                .filter(r -> r.getStatus() != CubilisReservation.Status.PENDING)
                .collect(Collectors.toList());
    }

    private void setHotelForReservations(Long hotelId, List<CubilisReservation> reservations) {
        Hotel hotel = hotelService.get(hotelId);
        for (CubilisReservation reservation : reservations) {
            reservation.setHotel(hotel);
        }
    }
}
