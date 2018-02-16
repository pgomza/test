package com.horeca.site.models.hotel.subscription;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
public class SubscriptionEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Integer level;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;

    @NotNull
    private Integer validityPeriod;

    /*
        the moment of expiration isn't necessarily createdAt + validityPeriod
        it's stored so that the entire history doesn't have to be replayed to calculate this value
     */
    @JsonIgnore
    private Timestamp expiresAt;

    SubscriptionEvent() {}

    public SubscriptionEvent(Integer level, Integer validityPeriod, Timestamp expiresAt) {
        this.level = level;
        this.validityPeriod = validityPeriod;
        this.expiresAt = expiresAt;
    }

    public SubscriptionEventView toView() {
        return new SubscriptionEventView(
                getLevel(), getCreatedAt().toString(), getValidityPeriod(), getExpiresAt().toString()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(Integer validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public Timestamp getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Timestamp expiresAt) {
        this.expiresAt = expiresAt;
    }
}
