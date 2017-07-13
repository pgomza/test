package com.horeca.site.services;

import com.horeca.site.exceptions.UnauthorizedException;
import com.horeca.site.models.cubilis.CubilisConnectionStatus;
import com.horeca.site.models.cubilis.CubilisReservation;
import com.horeca.site.models.cubilis.CubilisSettings;
import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.updates.ChangeInHotelEvent;
import com.horeca.site.repositories.CubilisSettingsRepository;
import com.horeca.site.services.services.StayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private HotelService hotelService;

    @Autowired
    private StayService stayService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public CubilisSettings getSettings(Long hotelId) {
        Hotel hotel = hotelService.get(hotelId);
        return hotel.getCubilisSettings();
    }

    public CubilisSettings updateSettings(Long hotelId, CubilisSettings updated) {
        CubilisSettings current = getSettings(hotelId);
        updated.setId(current.getId());
        return settingsRepository.save(updated);
    }

    public CubilisConnectionStatus getConnectionStatus(Long hotelId) {
        CubilisSettings settings = getSettings(hotelId);
        if (!settings.isEnabled()) {
            return new CubilisConnectionStatus(CubilisConnectionStatus.Status.DISABLED);
        }

        CubilisConnectionStatus.Status status =
                connectorService.checkConnectionStatus(settings.getLogin(), settings.getPassword());

        return new CubilisConnectionStatus(status);
    }

    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void fetchAndUpdateReservations() {
        List<Long> hotelIds = hotelService.getIdsOfCubilisEnabledHotels();
        Map<Long, CubilisSettings> hotelIdToSettings = hotelIds.stream()
                .collect(Collectors.toMap(Function.identity(), this::getSettings));

        for (Map.Entry<Long, CubilisSettings> entry : hotelIdToSettings.entrySet()) {
            Long hotelId = entry.getKey();
            CubilisSettings settings = entry.getValue();

            List<CubilisReservation> fetchedReservations = new ArrayList<>();
            try {
                fetchedReservations = connectorService.fetchReservations(settings.getLogin(), settings.getPassword());
            } catch (UnauthorizedException ex) {
                // disable the integration; otherwise requests with the wrong credentials would be sent
                // repeatedly causing the associated Cubilis account to become blocked
                CubilisSettings currentSettings = getSettings(hotelId);
                currentSettings.setEnabled(false);
                updateSettings(hotelId, currentSettings);
            }

            List<CubilisReservation> filteredReservations = filterFetchedReservations(hotelId, fetchedReservations);

            if (!filteredReservations.isEmpty()) {
                setHotelForReservations(hotelId, filteredReservations);

                if (settings.isMergingEnabled()) {
                    reservationService.merge(filteredReservations);
                } else {
                    reservationService.save(filteredReservations);
                }

                eventPublisher.publishEvent(new ChangeInHotelEvent(this, hotelId));
            }
        }
    }

    private List<CubilisReservation> filterFetchedReservations(Long hotelId, List<CubilisReservation> reservations) {
        Set<Long> alreadyMergedIds = stayService.getAllCubilisIdsInHotel(hotelId);
        Set<Long> pendingReservationIds = reservationService.getAll(hotelId).stream()
                .map(CubilisReservation::getId)
                .collect(Collectors.toSet());

        alreadyMergedIds.addAll(pendingReservationIds);

        return reservations.stream().filter(r -> !alreadyMergedIds.contains(r.getId())).collect(Collectors.toList());
    }

    private void setHotelForReservations(Long hotelId, List<CubilisReservation> reservations) {
        Hotel hotel = hotelService.get(hotelId);
        for (CubilisReservation reservation : reservations) {
            reservation.setHotel(hotel);
        }
    }
}
