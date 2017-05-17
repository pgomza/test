package com.horeca.site.services;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.models.notifications.NewStayEvent;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class NewStayEmailNotificationService implements ApplicationListener<NewStayEvent> {

    private static final Logger logger = Logger.getLogger(NewOrderEmailNotificationService.class);

    @Value("${appLink.appStore}")
    private String appLinkAppStore;
    @Value("${appLink.playStore}")
    private String appLinkPlayStore;


    @Autowired
    private JavaMailSender mailSender;

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
            throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        String messageBody =
                "<div>" +
                    "Dear " + guestName + "," +
                    "<br/><br/>" +
                    "Welcome to hotel " + hotelName + "." +
                    "<br/><br/>" +
                    "To enjoy fully all digital services of this hotel, we highly recommend you to use the Throdi " +
                    "application during your stay." +
                    "<br/><br/>" +
                    "The free Throdi application for iOS can be downloaded from the App Store here:" +
                    "<br/>" +
                    appLinkAppStore +
                    "<br/>" +
                    "The free Throdi application for Android can be downloaded from the Google Play Store here:" +
                    appLinkPlayStore +
                    "<br/><br/>" +
                    "Your personel check in code to start using the Throdi application is:" +
                    "<br/><br/>" +
                    "<b>" + pin + "</b>" +
                    "<br/><br/>" +
                    "Enjoy you stay." +
                    "<br/><br/>" +
                    "The Throdi Team" +
                    "<br>" +
                    "www.throdi.com" +
                "</div>";

        mimeMessage.setContent(messageBody, "text/html");
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
        helper.setTo(recipientEmail);
        helper.setSubject("Information about your stay");
        helper.setFrom("Throdi");
        mailSender.send(mimeMessage);
    }
}
