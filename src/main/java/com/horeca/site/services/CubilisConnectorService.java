package com.horeca.site.services;

import com.horeca.site.models.cubilis.CubilisConnectionStatus;
import com.horeca.site.models.cubilis.CubilisReservation;
import org.apache.log4j.Logger;
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
import java.util.ArrayList;
import java.util.List;

import static com.horeca.site.services.CubilisParserService.*;

@Service
@Transactional
public class CubilisConnectorService {

    private static final Logger logger = Logger.getLogger(CubilisConnectorService.class);

    private static final String RESERVATIONS_URL = "https://cubilis.eu/plugins/pms_ota/reservations.aspx";
    private static final String CONFIRMATIONS_URL = "https://cubilis.eu/plugins/pms_ota/confirmreservations.aspx";
    private static final int FETCH_TIME_SPAN = 10; // fetch reservations from the last 10 days

    private final CubilisParserService parserService = new CubilisParserService();

    public CubilisConnectionStatus.Status checkConnectionStatus(String cubilisLogin, String cubilisPassword) {
        try {
            String requestBody = createFetchRequest(cubilisLogin, cubilisPassword, LocalDate.now().minusDays(1));
            String responseRaw = postToCubilis(RESERVATIONS_URL, requestBody);
            String response = responseRaw.replaceAll("ï»¿", "");
            return getResponseOutcome(response);
        } catch (ParserConfigurationException | TransformerException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<CubilisReservation> fetchReservations(String cubilisLogin, String cubilisPassword) {
        try {
            String requestBody = createFetchRequest(cubilisLogin, cubilisPassword, LocalDate.now().minusDays(FETCH_TIME_SPAN));
            String responseRaw = postToCubilis(RESERVATIONS_URL, requestBody);
            String response = responseRaw.replaceAll("ï»¿", "");
            CubilisConnectionStatus.Status responseOutcome = getResponseOutcome(response);
            if (responseOutcome == CubilisConnectionStatus.Status.SUCCESS) {
                return getReservations(response);
            }
            else {
                return new ArrayList<>();
            }
        } catch (ParserConfigurationException | TransformerException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void confirmReservations(String cubilisLogin, String cubilisPassword, List<Long> ids) {
        try {
            String requestBody = createConfirmationRequest(cubilisLogin, cubilisPassword, ids);
            postToCubilis(RESERVATIONS_URL, requestBody);
        } catch (ParserConfigurationException | TransformerException e) {
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
