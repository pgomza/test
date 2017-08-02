package com.horeca.site.services;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.models.notifications.NewStayEvent;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@Service
public class NewStayEmailNotificationService implements ApplicationListener<NewStayEvent> {

    private static final Logger logger = Logger.getLogger(NewOrderEmailNotificationService.class);

    @Value("${appLink.appStore}")
    private String appLinkAppStore;
    @Value("${appLink.playStore}")
    private String appLinkPlayStore;

    @Autowired
    private EmailSenderService emailSenderService;

    @Override
    public void onApplicationEvent(NewStayEvent event) {
        String guestName = event.getGuest().getFirstName() + " " + event.getGuest().getLastName();
        String recipientEmail = event.getGuest().getEmail();
        String pin = event.getPin();
        try {

            if (recipientEmail == null || recipientEmail.isEmpty()) {
                throw new BusinessRuleViolationException("The recipient's email cannot be empty");
            }

            prepareAndSendMessage(recipientEmail, event.getHotelName(), guestName, pin);
        } catch (Exception e) {
            logger.error("Error while sending an email about a new stay to: " + recipientEmail);
            logger.error("Guest: " + guestName);
            logger.error("Pin: " + pin);
            logger.error("Exception message: " + e.getMessage());
            logger.error("Exception cause: " + e.getCause());
            throw new RuntimeException("There was an error while sending an email about a new stay to: " + recipientEmail, e);
        }
    }

    private void prepareAndSendMessage(String recipientEmail, String hotelName, String guestName, String pin)
            throws MessagingException, UnsupportedEncodingException {

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

        emailSenderService.send("Information about your stay", content, recipientEmail,
                "no-reply@throdi.com", "Throdi");
    }
}
