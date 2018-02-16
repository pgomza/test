CREATE INDEX Subscription_hotel_IND on Subscription(hotel_id);
CREATE INDEX SubscriptionEvent_subscription_IND on SubscriptionEvent(subscription_id);
CREATE INDEX SubscriptionEvent_expiresAt_IND on SubscriptionEvent(expiresAt);