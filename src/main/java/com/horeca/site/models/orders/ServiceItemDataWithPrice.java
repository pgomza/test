package com.horeca.site.models.orders;

import com.horeca.site.models.Price;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

/*
    a class for the common case when a service item has only the name and price fields
 */

@Entity
@Audited
public class ServiceItemDataWithPrice extends ServiceItemBaseData {

    @NotNull
    private Price price;

    ServiceItemDataWithPrice() {}

    public ServiceItemDataWithPrice(String name, Price price) {
        super(name);
        this.price = price;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }
}
