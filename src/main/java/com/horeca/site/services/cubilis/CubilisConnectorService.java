package com.horeca.site.services.cubilis;

import com.horeca.site.exceptions.UnauthorizedException;
import com.horeca.site.models.cubilis.CubilisConnectionStatus;
import com.horeca.site.models.cubilis.CubilisReservation;
import com.horeca.site.models.cubilis.CubilisRoomsPerHotel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.horeca.site.services.cubilis.CubilisParserService.*;

@Service
@Transactional
public class CubilisConnectorService {

    private static final String RESERVATIONS_URL = "https://cubilis.eu/plugins/pms_ota/reservations.aspx";
    private static final String CONFIRMATIONS_URL = "https://cubilis.eu/plugins/pms_ota/confirmreservations.aspx";
    private static final String ROOMS_URL = "https://cubilis.eu/plugins/pms_ota/accommodations.aspx";
    private static final int MAX_RESERVATIONS_PER_REQUEST = 25; // limit imposed by Cubilis

    CubilisConnectionStatus.Status checkConnectionStatus(String cubilisLogin, String cubilisPassword) {
        try {
            String requestBody = createFetchRoomsRequest(cubilisLogin, cubilisPassword);
            String responseRaw = postToCubilis(ROOMS_URL, requestBody);
            String response = responseRaw.replaceAll("ï»¿", "");
            return getResponseOutcome(response);
        } catch (ParserConfigurationException | TransformerException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    List<CubilisReservation> fetchReservations(String cubilisLogin, String cubilisPassword) {
        List<CubilisReservation> allReservations = new ArrayList<>();
        boolean fetchedAll = false;

        while (!fetchedAll) {
            List<CubilisReservation> reservations = doFetchReservations(cubilisLogin, cubilisPassword);
            List<Long> receivedIds =
                    reservations.stream().map(CubilisReservation::getId).collect(Collectors.toList());

            if (!receivedIds.isEmpty()) {
                confirmReservations(cubilisLogin, cubilisPassword, receivedIds);
            }

            allReservations.addAll(reservations);

            if (reservations.size() < MAX_RESERVATIONS_PER_REQUEST) {
                fetchedAll = true;
            }
        }

        return filterReservations(allReservations);
    }

    private List<CubilisReservation> doFetchReservations(String cubilisLogin, String cubilisPassword) {
        try {
            String requestBody = createFetchReservationsRequest(cubilisLogin, cubilisPassword);
            String responseRaw = postToCubilis(RESERVATIONS_URL, requestBody);
            String response = responseRaw.replaceAll("ï»¿", "");
            CubilisConnectionStatus.Status responseOutcome = getResponseOutcome(response);
            if (responseOutcome == CubilisConnectionStatus.Status.SUCCESS) {
                return getReservations(response);
            }
            else {
                throw new UnauthorizedException();
            }
        } catch (ParserConfigurationException | TransformerException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void confirmReservations(String cubilisLogin, String cubilisPassword, List<Long> ids) {
        try {
            String requestBody = createConfirmReservationsRequest(cubilisLogin, cubilisPassword, ids);
            postToCubilis(CONFIRMATIONS_URL, requestBody);
        } catch (ParserConfigurationException | TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    List<CubilisRoomsPerHotel> fetchAvailableRooms(String cubilisLogin, String cubilisPassword) {
        try {
            String requestBody = createFetchRoomsRequest(cubilisLogin, cubilisPassword);
            String responseRaw = postToCubilis(ROOMS_URL, requestBody);
            String response = responseRaw.replaceAll("ï»¿", "");
            CubilisConnectionStatus.Status responseOutcome = getResponseOutcome(response);
            if (responseOutcome == CubilisConnectionStatus.Status.SUCCESS) {
                return getRoomsPerHotelList(response);
            }
            else {
                throw new UnauthorizedException();
            }
        } catch (ParserConfigurationException | TransformerException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String postToCubilis(String url, String body) {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_XML);
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        return template.postForObject(url, request, String.class);
    }
}
