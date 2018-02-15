package com.horeca.site.repositories;

import com.horeca.site.models.hotel.subscription.Subscription;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.Set;

public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {

    Subscription findByHotelId(Long hotelId);

    @Query(
            value = "select top 1 e.level from Subscription s join SubscriptionEvent e on s.id = e.subscription_id " +
            "where s.hotel_id = :hotelId and getdate() < e.expiresAt order by e.expiresAt asc",
            nativeQuery = true
    )
    Integer getCurrentLevel(@Param("hotelId") Long hotelId);

    @Query(
            value = "select s.* from Subscription s join SubscriptionEvent e on s.id = e.subscription_id " +
                    "where e.expiresAt > :lastTimestampChecked",
            nativeQuery = true
    )
    Set<Subscription> getWithEventsNewerThan(@Param("lastTimestampChecked") Timestamp lastTimestampChecked);
}
