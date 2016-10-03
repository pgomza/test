package com.horeca.site.models.stay;

import javax.validation.constraints.NotNull;

public class StayStatusUPDATE {

    @NotNull
    private StayStatus status;

    public StayStatus getStatus() {
        return status;
    }

    public void setStatus(StayStatus status) {
        this.status = status;
    }
}
