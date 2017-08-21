package com.horeca.site.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;

@Embeddable
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Price {

    private BigDecimal value;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private String text;

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
