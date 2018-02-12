package com.horeca.site.repositories;

import com.horeca.site.models.hotel.subscription.Subscription;
import org.springframework.data.repository.CrudRepository;

public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {

    Subscription findByHotelId(Long hotelId);
}
