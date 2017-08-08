package com.horeca.site.services.cubilis;

import com.horeca.site.exceptions.UnauthorizedException;
import com.horeca.site.models.cubilis.CubilisConnectionStatus;
import com.horeca.site.models.cubilis.CubilisReservation;
import com.horeca.site.models.cubilis.CubilisRoomsPerHotel;
import org.joda.time.LocalDate;
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
import java.util.List;

import static com.horeca.site.services.cubilis.CubilisParserService.*;

@Service
@Transactional
public class CubilisConnectorService {

    private static final String RESERVATIONS_URL = "https://cubilis.eu/plugins/pms_ota/reservations.aspx";
    private static final String CONFIRMATIONS_URL = "https://cubilis.eu/plugins/pms_ota/confirmreservations.aspx";
    private static final String ROOMS_URL = "https://cubilis.eu/plugins/pms_ota/accommodations.aspx";
    private static final int FETCH_TIME_SPAN = 10; // fetch reservations from the last 10 days

    CubilisConnectionStatus.Status checkConnectionStatus(String cubilisLogin, String cubilisPassword) {
        try {
            String requestBody = createFetchReservationsRequest(cubilisLogin, cubilisPassword, LocalDate.now().minusDays(1));
            String responseRaw = postToCubilis(RESERVATIONS_URL, requestBody);
            String response = responseRaw.replaceAll("ï»¿", "");
            return getResponseOutcome(response);
        } catch (ParserConfigurationException | TransformerException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    List<CubilisReservation> fetchReservations(String cubilisLogin, String cubilisPassword) {
        try {
            String requestBody = createFetchReservationsRequest(cubilisLogin, cubilisPassword, LocalDate.now().minusDays(FETCH_TIME_SPAN));
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

    void confirmReservations(String cubilisLogin, String cubilisPassword, List<Long> ids) {
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
