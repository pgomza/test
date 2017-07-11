package com.horeca.site.services;

import com.horeca.site.models.cubilis.CubilisConnectionStatus;
import com.horeca.site.models.cubilis.CubilisCustomer;
import com.horeca.site.models.cubilis.CubilisReservation;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CubilisParserService {

    static String createFetchRequest(String login, String password, LocalDate sinceDate)
            throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbFactory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element rootElement =
                doc.createElementNS("http://www.opentravel.org/OTA/2003/05","OTA_HotelResRQ");
        rootElement.setAttribute("Version", "2.02");

        Element pos = createPOSElement(doc, login, password);

        // create the HotelReservations element
        Element hotelReservations = doc.createElement("HotelReservations");
        Element hotelReservation = doc.createElement("HotelReservation");
        hotelReservation.setAttribute("PurgeDate", sinceDate.toString("yyyy-MM-dd"));
        hotelReservations.appendChild(hotelReservation);

        rootElement.appendChild(pos);
        rootElement.appendChild(hotelReservations);

        return elementToString(rootElement);
    }

    static String createConfirmationRequest(String login, String password, List<Long> ids)
            throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbFactory.newDocumentBuilder();
        Document doc = builder.newDocument();

        Element rootElement = doc.createElement("OTA_NotifReportRQ");
        Element notifDetails = doc.createElement("NotifDetails");
        Element hotelNotifReport = doc.createElement("HotelNotifReport");
        Element hotelReservations = doc.createElement("HotelReservations");
        Element hotelReservation = doc.createElement("HotelReservation");

        Element pos = createPOSElement(doc, login, password);

        Element resGlobalInfo = doc.createElement("ResGlobalInfo");
        Element hotelReservationIDs = doc.createElement("HotelReservationIDs");
        for (Long id : ids) {
            Element hotelReservationID = doc.createElement("HotelReservationID");
            hotelReservationID.setAttribute("ResID_Value", id.toString());
            hotelReservationIDs.appendChild(hotelReservationID);
        }
        resGlobalInfo.appendChild(hotelReservationIDs);

        hotelReservation.appendChild(pos);
        hotelReservation.appendChild(resGlobalInfo);
        hotelReservations.appendChild(hotelReservation);
        hotelNotifReport.appendChild(hotelReservations);
        notifDetails.appendChild(hotelNotifReport);
        rootElement.appendChild(notifDetails);

        return elementToString(rootElement);
    }

    static List<CubilisReservation> getReservations(String body) throws ParserConfigurationException,
            IOException, SAXException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbFactory.newDocumentBuilder();
        Document doc = builder.parse(stringToInputStream(body));
        doc.getDocumentElement().normalize();

        List<CubilisReservation> cubilisReservations = new ArrayList<>();
        NodeList reservationNodes = doc.getElementsByTagName("HotelReservation");
        for (int i = 0; i < reservationNodes.getLength(); i++) {
            CubilisReservation cubilisReservation = new CubilisReservation();
            Element reservation = (Element) reservationNodes.item(i);

            Long reservationId = Long.valueOf(reservation.getAttribute("CreatorID"));
            cubilisReservation.setId(reservationId);

            String status = reservation.getAttribute("ResStatus");
            cubilisReservation.setStatus(status);

            Element timeSpan = getFirstElement(reservation.getElementsByTagName("TimeSpan"));
            String arrivalText = timeSpan.getAttribute("Start");
            cubilisReservation.setArrival(LocalDateTime.parse(arrivalText));
            String departureText = timeSpan.getAttribute("End");
            cubilisReservation.setDeparture(LocalDate.parse(departureText));

            List<Element> profiles = getElements(reservation.getElementsByTagName("Profile"));
            Optional<Element> mainProfileOptional = profiles.stream()
                    .filter(p -> "1".equals(p.getAttribute("RPH")))
                    .findAny();

            if (mainProfileOptional.isPresent()) { // though according to the specs this should always be the case
                Element mainProfile = mainProfileOptional.get();

                Element customer = getFirstElement(mainProfile.getElementsByTagName("Customer"));
                Element firstName = getFirstElement(customer.getElementsByTagName("GivenName"));
                Element lastName = getFirstElement(customer.getElementsByTagName("Surname"));
                Element email = getFirstElement(customer.getElementsByTagName("Email"));

                String firstNameText = firstName != null ? getElementText(firstName) : null;
                String lastNameText = lastName != null ? getElementText(lastName) : null;
                String emailText = email != null ? getElementText(email) : null;

                CubilisCustomer cubilisCustomer = new CubilisCustomer();
                cubilisCustomer.setFirstName(firstNameText);
                cubilisCustomer.setLastName(lastNameText);
                cubilisCustomer.setEmail(emailText);

                cubilisReservation.setCustomer(cubilisCustomer);
                cubilisReservations.add(cubilisReservation);
            }
        }

        return filterReservations(cubilisReservations);
    }

    static CubilisConnectionStatus.Status getResponseOutcome(String response) throws IOException, SAXException,
            ParserConfigurationException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbFactory.newDocumentBuilder();
        Document doc = builder.parse(stringToInputStream(response));
        Element success = getFirstElement(doc.getElementsByTagName("Success"));
        if (success != null) {
            return CubilisConnectionStatus.Status.SUCCESS;
        }
        else {
            List<Element> errors = getElements(doc.getElementsByTagName("Error"));
            if (!errors.isEmpty()) {
                for (Element error : errors) {
                    String errorCode = error.getAttribute("Code");
                    if ("401".equals(errorCode)) {
                        return CubilisConnectionStatus.Status.AUTHENTICATION_ERROR;
                    }
                }
            }
            return CubilisConnectionStatus.Status.UNKNOWN_ERROR;
        }
    }

    private static Element createPOSElement(Document document, String login, String password) {
        Element pos = document.createElement("POS");
        Element source = document.createElement("Source");
        Element requestorID = document.createElement("RequestorID");
        requestorID.setAttribute("Type", "1");
        requestorID.setAttribute("ID", login);
        requestorID.setAttribute("MessagePassword", password);
        source.appendChild(requestorID);
        pos.appendChild(source);
        return pos;
    }

    private static List<CubilisReservation> filterReservations(List<CubilisReservation> reservations) {
        List<CubilisReservation> remaining = new ArrayList<>();
        for (CubilisReservation reservation : reservations) {
            String status = reservation.getStatus();
            if ("Reserved".equals(status) || "Modify".equals(status) || "Waitlisted".equals(status)) {
                LocalDate departure = reservation.getDeparture();
                if (!departure.isBefore(LocalDate.now())) {
                    remaining.add(reservation);
                }
            }
        }
        return remaining;
    }

    private static Element getFirstElement(NodeList nodes) {
        if (nodes.getLength() > 0) {
            Node firstNode = nodes.item(0);
            if (firstNode.getNodeType() == Node.ELEMENT_NODE) {
                return (Element) firstNode;
            }
        }
        return null;
    }

    private static List<Element> getElements(NodeList nodes) {
        List<Element> elements = new ArrayList<>();
        if (nodes.getLength() > 0) {
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    elements.add((Element) node);
                }
            }
        }
        return elements;
    }

    private static String getElementText(Element element) {
        String text = element.getTextContent();
        if (text != null) {
            return text.trim();
        }
        return null;
    }

    private static String elementToString(Element element) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(element), new StreamResult(writer));
        return writer.getBuffer().toString().replaceAll("\n|\r", "");
    }

    private static InputStream stringToInputStream(String string) {
        return new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8));
    }
}
