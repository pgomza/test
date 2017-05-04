package com.horeca.site.services;

import com.horeca.site.models.guest.Guest;
import com.horeca.site.models.notifications.NewOrderEvent;
import com.horeca.site.models.stay.Stay;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class NewOrderEmailNotificationService implements ApplicationListener<NewOrderEvent> {

    private static final Logger logger = Logger.getLogger(NewOrderEmailNotificationService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(NewOrderEvent event) {
        Stay stay = event.getStay();
        String recipientEmail = stay.getHotel().getNotificationSettings().getEmail();
        String service = event.getServiceType().toString();
        Guest guest = stay.getGuest();

        try {
            prepareAndSendMessage(recipientEmail, service, guest);
        } catch (MessagingException e) {
            logger.error("Error while sending an email about a new order to: " + recipientEmail);
            logger.error("Guest: " + guest.getId() + ", service: " + service);
        }
    }

    private void prepareAndSendMessage(String recipientEmail, String service, Guest guest)
            throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        String messageBody =
                "<div>" +
                        "Hi," +
                        "<br/><br/>" +
                        "we would like to inform you that <b>"
                        + guest.getFirstName() + " " + guest.getLastName() +
                        "</b> has just placed an order through our <b>" + service + "</b> service." +
                        "<br/><br/>" +
                        "Cheers," +
                        "<br/>" +
                        "The Throdi Team" +
                        "</div>";
        mimeMessage.setContent(messageBody, "text/html");
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
        helper.setTo(recipientEmail);
        helper.setSubject("A new order in the " + service + " service");
        helper.setFrom("Throdi");
        mailSender.send(mimeMessage);
    }
}
