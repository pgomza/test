package com.horeca.site.models.orders.roomservice;

import com.horeca.site.models.hotel.services.roomservice.RoomServiceItem;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(indexes = @Index(name = "room_service_order_id", columnList = "room_service_order_id"))
@Audited
public class RoomServiceOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private RoomServiceItem item;

    @NotNull
    private Integer count;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoomServiceItem getItem() {
        return item;
    }

    public void setItem(RoomServiceItem item) {
        this.item = item;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
