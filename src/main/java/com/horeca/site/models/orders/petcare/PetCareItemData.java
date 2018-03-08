package com.horeca.site.models.orders.petcare;

import com.horeca.site.models.Price;
import com.horeca.site.models.hotel.translation.Translatable;
import com.horeca.site.models.orders.ServiceItemBaseData;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@Audited
public class PetCareItemData extends ServiceItemBaseData {

    @NotNull
    private Price price;

    @Translatable
    private String description;

    PetCareItemData() {}

    public PetCareItemData(String name, Price price, String description) {
        super(name);
        this.price = price;
        this.description = description;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
