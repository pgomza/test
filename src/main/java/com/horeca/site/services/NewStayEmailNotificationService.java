package com.horeca.site.services;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.models.guest.Guest;
import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.hotel.images.FileLink;
import com.horeca.site.models.notifications.NewStayEvent;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.services.services.StayService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Service
public class NewStayEmailNotificationService implements ApplicationListener<NewStayEvent> {

    private static final Logger logger = Logger.getLogger(NewOrderEmailNotificationService.class);

    private static final String EMAIL_SUBJECT = "Information about your stay";
    private static final String EMAIL_ADDRESS_FROM = "no-reply@throdi.com";
    private static final String EMAIL_NAME_FROM = "Throdi";

    @Value("${emailLabs.active}")
    private Boolean isEmailLabsActive;

    @Value("${appLink.appStore}")
    private String appLinkAppStore;

    @Value("${appLink.playStore}")
    private String appLinkPlayStore;

    @Value("${appImg.appStore}")
    private String appImgAppStore;

    @Value("${appImg.playStore}")
    private String appImgPlayStore;

    @Autowired
    private EmailSenderService emailSenderService;

    @Override
    public void onApplicationEvent(NewStayEvent event) {
        Stay stay = event.getStay();
        String recipientEmail = stay.getGuest().getEmail();
        try {

            if (recipientEmail == null || recipientEmail.isEmpty()) {
                throw new BusinessRuleViolationException("The recipient's email cannot be empty");
            }

            if (isEmailLabsActive) {
                sendTemplate(event);
            }
            else {
                sendStandard(event);
            }

        } catch (Exception e) {
            logger.error("Error while sending the email about a new stay to: " + recipientEmail);
            logger.error("Pin: " + stay.getPin());
            logger.error("Exception message: " + e.getMessage());
            logger.error("Exception cause: " + e.getCause());
            throw new RuntimeException("There was an error while sending an email about a new stay to: " + recipientEmail, e);
        }
    }

    private void sendTemplate(NewStayEvent event) throws UnsupportedEncodingException, MessagingException {
        Stay stay = event.getStay();
        Hotel hotel = stay.getHotel();

        String hotelLink = "";
        if (!hotel.getImages().isEmpty()) {
            FileLink fileLink = hotel.getImages().get(0);
            hotelLink = fileLink.getUrl();
        }

        String hotelName = hotel.getName();
        String hotelAddress = hotel.getAddress();
        String hotelMail = hotel.getEmail();
        if (hotelMail == null) hotelMail = "";
        String hotelPhone = hotel.getPhone();
        if (hotelPhone == null) hotelPhone = "";

        Guest guest = stay.getGuest();
        String guestName = guest.getFirstName() + " " + guest.getLastName();
        String guestMail = guest.getEmail();

        String arrival = stay.getFromDate().toString("dd-MM-yyyy");
        String departure = stay.getToDate().toString("dd-MM-yyyy");

        Map<String, String> vars = new HashMap<>();
        vars.put("HOTELLINK", hotelLink);
        vars.put("HOTELNAME", hotelName);
        vars.put("HOTELADDRESS", hotelAddress);
        vars.put("HOTELMAIL", hotelMail);
        vars.put("HOTELPHONE", hotelPhone);

        vars.put("GUESTNAME", guestName);
        vars.put("GUESTMAIL", guestMail);

        vars.put("ARRIVAL", arrival);
        vars.put("DEPARTURE", departure);

        vars.put("IOSLINK", appLinkAppStore);
        vars.put("ANDROIDLINK", appLinkPlayStore);
        vars.put("IOSIMGLINK", appImgAppStore);
        vars.put("ANDROIDIMGLINK", appImgPlayStore);

        vars.put("PIN", stay.getPin());

        emailSenderService.sendTemplate(EMAIL_SUBJECT, guestMail, EMAIL_ADDRESS_FROM, EMAIL_NAME_FROM, vars);
    }

    private void sendStandard(NewStayEvent event) throws UnsupportedEncodingException, MessagingException {
        Stay stay = event.getStay();
        Hotel hotel = stay.getHotel();
        Guest guest = stay.getGuest();

        String guestMail = guest.getEmail();
        String guestName = guest.getFirstName() + " " + guest.getLastName();
        String hotelName = hotel.getName();
        String pin = stay.getPin();

        boolean hotelNameBeginsWithHotel = false;
        if (hotelName.length() >= 5 && hotelName.substring(0, 5).toLowerCase().equals("hotel"))
            hotelNameBeginsWithHotel = true;

        String content =
                "<div>" +
                        "Dear " + guestName + "," +
                        "<br/><br/>" +
                        "Welcome to " + (hotelNameBeginsWithHotel ? hotelName : "hotel " + hotelName) + "." +
                        "<br/><br/>" +
                        "To fully enjoy all digital services of this hotel, we highly recommend you to use the Throdi " +
                        "application during your stay." +
                        "<br/><br/>" +
                        "The free Throdi application for iOS can be downloaded from the App Store here:" +
                        "<br/>" +
                        appLinkAppStore +
                        "<br/>" +
                        "The free Throdi application for Android can be downloaded from the Google Play Store here:" +
                        "<br/>" +
                        appLinkPlayStore +
                        "<br/><br/>" +
                        "Your personal check-in code to start using the Throdi application is:" +
                        "<br/><br/>" +
                        "<b>" + pin + "</b>" +
                        "<br/><br/>" +
                        "Enjoy your stay." +
                        "<br/><br/>" +
                        "The Throdi Team" +
                        "<br>" +
                        "www.throdi.com" +
                        "</div>";

        emailSenderService.sendStandard(EMAIL_SUBJECT, content, guestMail, EMAIL_ADDRESS_FROM, EMAIL_NAME_FROM);
    }
}
