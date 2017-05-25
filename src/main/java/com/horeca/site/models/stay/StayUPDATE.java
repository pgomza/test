package com.horeca.site.models.stay;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.joda.time.LocalDate;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

public class StayUPDATE {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate fromDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate toDate;

    @NotNull
    private String roomNumber;

    private String wifiPassword;

    private String doorKey;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StayStatus status;

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getWifiPassword() {
        return wifiPassword;
    }

    public void setWifiPassword(String wifiPassword) {
        this.wifiPassword = wifiPassword;
    }

    public String getDoorKey() {
        return doorKey;
    }

    public void setDoorKey(String doorKey) {
        this.doorKey = doorKey;
    }

    public StayStatus getStatus() {
        return status;
    }

    public void setStatus(StayStatus status) {
        this.status = status;
    }
}
