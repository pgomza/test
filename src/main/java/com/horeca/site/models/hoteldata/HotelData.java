package com.horeca.site.models.hoteldata;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.horeca.site.models.hotel.images.FileLink;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

@Entity
public class HotelData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
        general info
     */

    @NotEmpty
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotEmpty
    private String address;

    @NotNull
    private Double longitude;

    @NotNull
    private Double latitude;

    private String email;

    private String website;

    private String phone;

    private String fax;

    private Float starRating;

    private Integer rooms;

    private Integer lowestPriceUSD;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime checkIn;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime checkOut;

    private String propertyType;

    private String chain;

    /*
        ratings
     */

    private Float ratingOverall;

    private String ratingOverallText;

    private Float ratingCleanliness;

    private Float ratingDining;

    private Float ratingFacilities;

    private Float ratingLocation;

    private Float ratingRooms;

    private Float ratingService;

    @Column(columnDefinition = "TEXT")
    private String ratingMainPoints;

    /*
        reviews
     */

    private Integer reviewsCount;

    @Column(columnDefinition = "TEXT")
    private String reviewsPositive;

    @Column(columnDefinition = "TEXT")
    private String reviewsNegative;

    /*
        features
     */

    @Column(columnDefinition = "TEXT")
    private String features;

    @ManyToMany
    @JoinTable(
            name = "HotelData_FileLink",
            joinColumns = @JoinColumn(name = "HotelData_id"),
            inverseJoinColumns = @JoinColumn(name = "FileLink_id")
    )
    private List<FileLink> images;

    public HotelDataView toView() {
        HotelDataView hotelView = new HotelDataView();

        /*
            general info
         */

        hotelView.setId(getId());
        hotelView.setName(getName());
        hotelView.setDescription(getDescription());
        hotelView.setAddress(getAddress());
        hotelView.setLongitude(getLongitude());
        hotelView.setLatitude(getLatitude());
        hotelView.setEmail(getEmail());
        hotelView.setWebsite(getWebsite());
        hotelView.setPhone(getPhone());
        hotelView.setFax(getFax());
        hotelView.setStarRating(getStarRating());
        hotelView.setRooms(getRooms());
        hotelView.setLowestPriceUSD(getLowestPriceUSD());
        hotelView.setCheckIn(getCheckIn());
        hotelView.setCheckOut(getCheckOut());
        hotelView.setPropertyType(getPropertyType());
        hotelView.setChain(getChain());

        /*
            ratings
         */

        HotelRatings hotelRatings = new HotelRatings();
        hotelRatings.setOverall(getRatingOverall());
        hotelRatings.setOverallText(getRatingOverallText());
        hotelRatings.setCleanliness(getRatingCleanliness());
        hotelRatings.setDining(getRatingDining());
        hotelRatings.setFacilities(getRatingFacilities());
        hotelRatings.setLocation(getRatingLocation());
        hotelRatings.setRooms(getRatingRooms());
        hotelRatings.setService(getRatingService());

        if (getRatingMainPoints() != null) {
            List<String> mainPoints = Arrays.asList(getRatingMainPoints().split(";"));
            hotelRatings.setMainPoints(mainPoints);
        }
        hotelView.setRatings(hotelRatings);

        /*
            reviews
         */

        HotelReviews hotelReviews = new HotelReviews();
        hotelReviews.setCount(getReviewsCount());
        hotelReviews.setPositive(getReviewsPositive());
        hotelReviews.setNegative(getReviewsNegative());
        hotelView.setReviews(hotelReviews);

        /*
            features
         */

        if (getFeatures() != null) {
            List<String> features = Arrays.asList(getFeatures().split(";"));
            hotelView.setFeatures(features);
        }

        /*
            images
         */

        hotelView.setImages(getImages());

        return hotelView;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public Float getStarRating() {
        return starRating;
    }

    public void setStarRating(Float starRating) {
        this.starRating = starRating;
    }

    public Integer getRooms() {
        return rooms;
    }

    public void setRooms(Integer rooms) {
        this.rooms = rooms;
    }

    public Integer getLowestPriceUSD() {
        return lowestPriceUSD;
    }

    public void setLowestPriceUSD(Integer lowestPriceUSD) {
        this.lowestPriceUSD = lowestPriceUSD;
    }

    public LocalTime getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalTime checkIn) {
        this.checkIn = checkIn;
    }

    public LocalTime getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalTime checkOut) {
        this.checkOut = checkOut;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getChain() {
        return chain;
    }

    public void setChain(String chain) {
        this.chain = chain;
    }

    public Float getRatingOverall() {
        return ratingOverall;
    }

    public void setRatingOverall(Float ratingOverall) {
        this.ratingOverall = ratingOverall;
    }

    public String getRatingOverallText() {
        return ratingOverallText;
    }

    public void setRatingOverallText(String ratingOverallText) {
        this.ratingOverallText = ratingOverallText;
    }

    public Float getRatingCleanliness() {
        return ratingCleanliness;
    }

    public void setRatingCleanliness(Float ratingCleanliness) {
        this.ratingCleanliness = ratingCleanliness;
    }

    public Float getRatingDining() {
        return ratingDining;
    }

    public void setRatingDining(Float ratingDining) {
        this.ratingDining = ratingDining;
    }

    public Float getRatingFacilities() {
        return ratingFacilities;
    }

    public void setRatingFacilities(Float ratingFacilities) {
        this.ratingFacilities = ratingFacilities;
    }

    public Float getRatingLocation() {
        return ratingLocation;
    }

    public void setRatingLocation(Float ratingLocation) {
        this.ratingLocation = ratingLocation;
    }

    public Float getRatingRooms() {
        return ratingRooms;
    }

    public void setRatingRooms(Float ratingRooms) {
        this.ratingRooms = ratingRooms;
    }

    public Float getRatingService() {
        return ratingService;
    }

    public void setRatingService(Float ratingService) {
        this.ratingService = ratingService;
    }

    public String getRatingMainPoints() {
        return ratingMainPoints;
    }

    public void setRatingMainPoints(String ratingMainPoints) {
        this.ratingMainPoints = ratingMainPoints;
    }

    public Integer getReviewsCount() {
        return reviewsCount;
    }

    public void setReviewsCount(Integer reviewsCount) {
        this.reviewsCount = reviewsCount;
    }

    public String getReviewsPositive() {
        return reviewsPositive;
    }

    public void setReviewsPositive(String reviewsPositive) {
        this.reviewsPositive = reviewsPositive;
    }

    public String getReviewsNegative() {
        return reviewsNegative;
    }

    public void setReviewsNegative(String reviewsNegative) {
        this.reviewsNegative = reviewsNegative;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public List<FileLink> getImages() {
        return images;
    }

    public void setImages(List<FileLink> images) {
        this.images = images;
    }
}
