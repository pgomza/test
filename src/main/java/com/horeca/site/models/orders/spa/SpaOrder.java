package com.horeca.site.models.orders.spa;

import com.horeca.site.models.Viewable;
import com.horeca.site.models.hotel.services.spa.SpaItem;
import com.horeca.site.models.orders.OrderStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class SpaOrder implements Viewable<SpaOrderView> {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private OrderStatus status = OrderStatus.NEW;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private SpaItem item;

    @NotNull
    private String time;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public SpaItem getItem() {
        return item;
    }

    public void setItem(SpaItem item) {
        this.item = item;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public SpaOrderView toView(String preferredLanguage, String defaultLanguage) {
        SpaOrderView view = new SpaOrderView();
        view.setId(getId());
        view.setStatus(getStatus());
        view.setItem(getItem().toView(preferredLanguage, defaultLanguage));
//        view.setTime(getTime().toString("dd-MM-yyyy HH:mm"));
        view.setTime(getTime());

        return view;
    }
}
