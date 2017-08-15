package com.horeca.site.models.cubilis;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.horeca.site.models.guest.Guest;
import com.horeca.site.models.hotel.Hotel;
import org.hibernate.envers.Audited;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Audited
public class CubilisReservation {

    public enum Status {
        @JsonProperty("Pending")
        PENDING("Pending"),

        @JsonProperty("New")
        NEW("New"),

        @JsonProperty("Modified")
        MODIFIED("Modified"),

        @JsonProperty("Cancelled")
        CANCELLED("Cancelled"),

        @JsonProperty("Undefined")
        UNDEFINED("Undefined");

        private String value;

        Status(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    @Id
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @OneToOne
    @JoinColumn(name = "guest_id")
    private Guest guest;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MMddTHH:mm:ss")
    private LocalDateTime arrival;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate departure;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "customer_id")
    private CubilisCustomer customer;

    @NotNull
    private Integer guestCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getArrival() {
        return arrival;
    }

    public void setArrival(LocalDateTime arrival) {
        this.arrival = arrival;
    }

    public LocalDate getDeparture() {
        return departure;
    }

    public void setDeparture(LocalDate departure) {
        this.departure = departure;
    }

    public CubilisCustomer getCustomer() {
        return customer;
    }

    public void setCustomer(CubilisCustomer customer) {
        this.customer = customer;
    }

    public Integer getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(Integer guestCount) {
        this.guestCount = guestCount;
    }

    public CubilisReservationUpdate toView() {
        CubilisReservationUpdate view = new CubilisReservationUpdate();
        view.setId(getId());
        view.setArrival(getArrival().toLocalDate());
        view.setDeparture(getDeparture());
        view.setCubilisCustomer(getCustomer());
        view.setGuestCount(getGuestCount());

        return view;
    }

    public static Status fromCubilisResStatus(String resStatus) {
        String resStatusLowerCase = resStatus.toLowerCase();
        switch (resStatusLowerCase) {
            case "waitlisted": return Status.PENDING;
            case "reserved": return Status.NEW;
            case "modify": return Status.MODIFIED;
            case "cancelled": return Status.CANCELLED;
            case "request denied": return Status.CANCELLED;
            default: return Status.UNDEFINED;
        }
    }
}
