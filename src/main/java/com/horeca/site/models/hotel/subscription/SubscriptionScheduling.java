package com.horeca.site.models.hotel.subscription;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
public class SubscriptionScheduling {

    @Id
    private Long id;

    @NotNull
    private Timestamp lastTimestampChecked;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getLastTimestampChecked() {
        return lastTimestampChecked;
    }

    public void setLastTimestampChecked(Timestamp lastTimestampChecked) {
        this.lastTimestampChecked = lastTimestampChecked;
    }
}
