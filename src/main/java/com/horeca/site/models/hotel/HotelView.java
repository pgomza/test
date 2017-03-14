package com.horeca.site.models.hotel;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.horeca.site.models.hotel.images.FileLink;
import com.horeca.site.models.hotel.information.UsefulInformation;
import com.horeca.site.models.hotel.roomdirectory.RoomDirectory;
import com.horeca.site.models.hotel.services.AvailableServiceViewSimplified;

import java.util.List;
import java.util.Set;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class HotelView {
	
	private Long id;
	private String name;
	private String description;
	private String address;
	private String email;
	private String website;
	private String phone;
	private String bookingUrl;
	private String fax;
	private Float starRating;
	private Integer rooms;
	private Float ratingOverall;
	private String ratingOverallText;
	private String propertyType;
	private String chain;
	private Double longitude;
	private Double latitude;
	private Boolean throdiPartner;
	private UsefulInformation usefulInformation;
	private RoomDirectory roomDirectory;
	private List<AvailableServiceViewSimplified> services;
	private Set<FileLink> images;

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

	public String getBookingUrl() {
		return bookingUrl;
	}

	public void setBookingUrl(String bookingUrl) {
		this.bookingUrl = bookingUrl;
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

	public Boolean getThrodiPartner() {
		return throdiPartner;
	}

	public void setThrodiPartner(Boolean throdiPartner) {
		this.throdiPartner = throdiPartner;
	}

	public UsefulInformation getUsefulInformation() {
		return usefulInformation;
	}

	public void setUsefulInformation(UsefulInformation usefulInformation) {
		this.usefulInformation = usefulInformation;
	}

	public RoomDirectory getRoomDirectory() {
		return roomDirectory;
	}

	public void setRoomDirectory(RoomDirectory roomDirectory) {
		this.roomDirectory = roomDirectory;
	}

	public List<AvailableServiceViewSimplified> getServices() {
		return services;
	}

	public void setServices(List<AvailableServiceViewSimplified> services) {
		this.services = services;
	}

	public Set<FileLink> getImages() {
		return images;
	}

	public void setImages(Set<FileLink> images) {
		this.images = images;
	}
}
