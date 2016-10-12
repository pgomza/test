package com.horeca.site.models.orders.spa;

import com.horeca.site.models.Viewable;
import com.horeca.site.models.hotel.services.spa.SpaItem;
import com.horeca.site.models.orders.OrderStatus;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Parameter;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import javax.persistence.*;
import javax.persistence.Entity;
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
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime",
    parameters = { @Parameter(name = "databaseZone", value = "UTC"),
                    @Parameter(name = "javaZone", value = "UTC")})
    //given the parameters it could be as well just LocalDateTime (no time zone stored)
    //the params are subject to change though
    private DateTime time;

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

    public DateTime getTime() {
        return time;
    }

    public void setTime(DateTime time) {
        this.time = time;
    }

    @Override
    public SpaOrderView toView(String preferredLanguage, String defaultLanguage) {
        SpaOrderView view = new SpaOrderView();
        view.setId(getId());
        view.setStatus(getStatus());
        view.setItem(getItem().toView(preferredLanguage, defaultLanguage));
        view.setTime(getTime().toString("dd-MM-yyyy HH:mm"));

        return view;
    }
}
