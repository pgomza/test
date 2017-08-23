package com.horeca.site.models.stay;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum StayStatus {
    @JsonProperty("NEW")
    NEW,

    @JsonProperty("ACTIVE")
    ACTIVE,

    @JsonProperty("FINISHED")
    FINISHED
}
