package com.horeca.site.models.hoteldata;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.util.List;

@Entity
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class HotelReviews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer count;

    @ElementCollection
    @Column(columnDefinition = "TEXT")
    private List<String> positive;

    @ElementCollection
    @Column(columnDefinition = "TEXT")
    private List<String> negative;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<String> getPositive() {
        return positive;
    }

    public void setPositive(List<String> positive) {
        this.positive = positive;
    }

    public List<String> getNegative() {
        return negative;
    }

    public void setNegative(List<String> negative) {
        this.negative = negative;
    }
}
