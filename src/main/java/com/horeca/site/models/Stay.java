package com.horeca.site.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Stay {

    public enum Status { NEW, ACTIVE, FINISHED }

    @Id
    private String pin;

    @NotNull
    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private Status status = Status.NEW;

    @NotEmpty
    @ApiModelProperty(required = true)
    private String someInfo;

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getPin() {
        return pin;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getSomeInfo() {
        return someInfo;
    }

    public void setSomeInfo(String someInfo) {
        this.someInfo = someInfo;
    }
}
