package com.horeca.site.models.orders.housekeeping;

import com.horeca.site.models.orders.ServiceItemBaseData;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;

@Entity
@Audited
public class HousekeepingItemData extends ServiceItemBaseData {

    HousekeepingItemData() {}

    public HousekeepingItemData(String name) {
        super(name);
    }
}
