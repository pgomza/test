package com.horeca.site.models.orders.tableordering;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.joda.time.LocalDateTime;

import javax.validation.constraints.NotNull;

public class TableOrderingOrderPOST {

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    public LocalDateTime time;

    @NotNull
    public Integer numberOfPeople;
}
