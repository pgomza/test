package com.horeca.site.models.hoteldata;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.util.List;

@Entity
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class HotelRatings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Float overall;

    private String overallText;

    private Float cleanliness;

    private Float dining;

    private Float facilities;

    private Float location;

    private Float rooms;

    private Float service;

    @ElementCollection
    @Column(columnDefinition = "TEXT")
    private List<String> mainPoints;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getOverall() {
        return overall;
    }

    public void setOverall(Float overall) {
        this.overall = overall;
    }

    public String getOverallText() {
        return overallText;
    }

    public void setOverallText(String overallText) {
        this.overallText = overallText;
    }

    public Float getCleanliness() {
        return cleanliness;
    }

    public void setCleanliness(Float cleanliness) {
        this.cleanliness = cleanliness;
    }

    public Float getDining() {
        return dining;
    }

    public void setDining(Float dining) {
        this.dining = dining;
    }

    public Float getFacilities() {
        return facilities;
    }

    public void setFacilities(Float facilities) {
        this.facilities = facilities;
    }

    public Float getLocation() {
        return location;
    }

    public void setLocation(Float location) {
        this.location = location;
    }

    public Float getRooms() {
        return rooms;
    }

    public void setRooms(Float rooms) {
        this.rooms = rooms;
    }

    public Float getService() {
        return service;
    }

    public void setService(Float service) {
        this.service = service;
    }

    public List<String> getMainPoints() {
        return mainPoints;
    }

    public void setMainPoints(List<String> mainPoints) {
        this.mainPoints = mainPoints;
    }
}
