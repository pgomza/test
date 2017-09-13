package com.horeca.site.services;

import com.horeca.site.models.stay.Stay;
import com.horeca.site.models.stay.StayStatus;
import com.horeca.site.models.updates.ChangeInStayEvent;
import com.horeca.site.services.services.StayService;
import com.windowsazure.messaging.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.HashSet;

@Service
@Transactional
public class PushNotificationUpdatesService implements ApplicationListener<ChangeInStayEvent> {

    private static final Logger logger = Logger.getLogger(PushNotificationUpdatesService.class);
    private static final String ANDROID_NOTIFICATION_BODY = "{\"data\":{\"message\":\"UPDATE\"}}\n";
    private static final String APPLE_NOTIFICATION_BODY = "{\"aps\":{\"alert\":\"UPDATE\"}}\n";

    private INotificationHub notificationHub;

    @Autowired
    private StayService stayService;

    @Value("${notificationHub.connectionString}")
    private String notificationHubConnectionString;

    @Value("${notificationHub.name}")
    private String notificationHubName;

    @PostConstruct
    private void init() {
        notificationHub = new NotificationHub(notificationHubConnectionString, notificationHubName);
    }

    @Override
    public void onApplicationEvent(ChangeInStayEvent event) {
        handleChangeInStay(event.getPin());
    }

    private void handleChangeInStay(String pin) {
        Stay stay = stayService.getWithoutCheckingStatus(pin);
        if (stay.getStatus() == StayStatus.ACTIVE) {
            try {
                CollectionResult collectionResult = notificationHub.getRegistrationsByTag(pin);
                for (Registration registration : collectionResult.getRegistrations()) {
                    sendNotificationIfSupported(registration, pin);
                }
            } catch (NotificationHubsException e) {
                logger.error("Exception while sending notification to " + pin);
                logger.error("Exception message: " + e.getMessage());
                logger.error("Exception cause: " + e.getCause());
            }
        }
    }

    private void sendNotificationIfSupported(Registration registration, String tag) throws NotificationHubsException {
        Notification notification = null;
        if (registration instanceof GcmRegistration) {
            notification = Notification.createGcmNotification(ANDROID_NOTIFICATION_BODY);
        }
        else if (registration instanceof AppleRegistration) {
            notification = Notification.createAppleNotification(APPLE_NOTIFICATION_BODY);
        }

        if (notification != null) {
            notificationHub.sendNotification(notification, new HashSet<>(Collections.singletonList(tag)));
        }
    }
}
