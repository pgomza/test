package com.horeca.site.services;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.models.guest.Guest;
import com.horeca.site.models.hotel.services.AvailableServiceType;
import com.horeca.site.models.notifications.NotificationSettings;
import com.horeca.site.models.notifications.OrderEvent;
import com.horeca.site.models.orders.dnd.DndOrder.Status;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.services.services.StayService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@Service
public class OrderEmailNotificationService implements ApplicationListener<OrderEvent> {

    private static final Logger logger = Logger.getLogger(OrderEmailNotificationService.class);
    private static final String EMAIL_ADDRESS_FROM = "no-reply@throdi.com";
    private static final String EMAIL_NAME_FROM = "Throdi";

    @Autowired
    private StayService stayService;

    @Autowired
    private EmailSenderService emailSenderService;

    @Override
    public void onApplicationEvent(OrderEvent event) {
        String pin = event.getPin();
        Stay stay = stayService.getWithoutCheckingStatus(pin);
        NotificationSettings notificationSettings = stay.getHotel().getNotificationSettings();
        if (notificationSettings == null) {
            throw new BusinessRuleViolationException("An email about a new order cannot be sendStandard because the " +
            "notification service's settings haven't been specified");
        }

        AvailableServiceType requestedServiceType = event.getServiceType();
        if (shouldNotificationBeSend(notificationSettings, requestedServiceType)) {
            String recipientEmail = notificationSettings.getEmail();
            Guest guest = stay.getGuest();

            try {
                if (requestedServiceType == AvailableServiceType.DND) {
                    Status currentDndStatus = stay.getOrders().getDnd().getStatus();
                    sendDndUpdateMessage(guest, currentDndStatus);
                }
                else {
                    sendNewOrderMessage(guest, requestedServiceType);
                }
            } catch (Exception e) {
                logger.error("Error while sending an email about a new order to: " + recipientEmail);
                logger.error("Guest: " + guest.getId() + ", service: " + requestedServiceType);
                logger.error("Exception message: " + e.getMessage());
                logger.error("Exception cause: " + e.getCause());
                throw new RuntimeException("There was an error while sending a new order to: " + recipientEmail, e);
            }
        }
    }

    private boolean shouldNotificationBeSend(NotificationSettings settings, AvailableServiceType requestedService) {
        switch (requestedService) {
            case BREAKFAST: return settings.isBreakfast();
            case CARPARK: return settings.isCarPark();
            case ROOMSERVICE: return settings.isRoomService();
            case SPA: return settings.isSpa();
            case PETCARE: return settings.isPetCare();
            case TAXI: return settings.isTaxi();
            case HOUSEKEEPING: return settings.isHousekeeping();
            case TABLEORDERING: return settings.isTableOrdering();
            case BAR: return settings.isBar();
            case RENTAL: return settings.isRental();
            case DND: return settings.isDnd();
            default: return false;
        }
    }

    private void sendNewOrderMessage(Guest guest, AvailableServiceType serviceType)
            throws MessagingException, UnsupportedEncodingException {

        String serviceName = serviceType.toString();

        String content =
                "<div>" +
                        "Hi," +
                        "<br/><br/>" +
                        "we would like to inform you that <b>"
                        + guest.getFirstName() + " " + guest.getLastName() +
                        "</b> has just placed an order through our <b>" + serviceName + "</b> service." +
                        "<br/><br/>" +
                        "Cheers," +
                        "<br/>" +
                        "The Throdi Team" +
                        "</div>";

        emailSenderService.sendStandard("A new order in the " + serviceName + " service", content,
                guest.getEmail(), EMAIL_ADDRESS_FROM, EMAIL_NAME_FROM);
    }

    private void sendDndUpdateMessage(Guest guest, Status status) throws UnsupportedEncodingException, MessagingException {
        String statusString = status == Status.ENABLED ? "enabled" : "disabled";

        String content =
                "<div>" +
                "   Hi," +
                "   <br/><br/>" +
                "   we would like to inform you that <b>"
                    + guest.getFirstName() + " " + guest.getLastName() +
                "   </b> has just  <b>" + statusString + " </b> the Do Not Disturb mode." +
                "   <br/><br/>" +
                "   Cheers," +
                "   <br/>" +
                "   The Throdi Team" +
                "</div>";

        emailSenderService.sendStandard("DND mode change", content, guest.getEmail(), EMAIL_ADDRESS_FROM,
                EMAIL_NAME_FROM);
    }
}
