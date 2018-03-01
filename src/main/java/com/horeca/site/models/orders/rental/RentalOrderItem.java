package com.horeca.site.models.orders.rental;

import com.horeca.site.models.hotel.translation.Translatable;
import com.horeca.site.models.orders.ServiceItemDataWithPrice;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Audited
public class RentalOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Translatable
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "service_item_id")
    private ServiceItemDataWithPrice item;

    @NotNull
    private Integer count;

    RentalOrderItem() {}

    public RentalOrderItem(ServiceItemDataWithPrice item, Integer count) {
        this.item = item;
        this.count = count;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ServiceItemDataWithPrice getItem() {
        return item;
    }

    public void setItem(ServiceItemDataWithPrice item) {
        this.item = item;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
