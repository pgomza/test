package com.horeca.site.models.hotel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.horeca.site.models.Currency;
import com.horeca.site.models.guest.Guest;
import com.horeca.site.models.hotel.images.FileLink;
import com.horeca.site.models.hotel.information.UsefulInformation;
import com.horeca.site.models.hotel.roomdirectory.RoomDirectory;
import com.horeca.site.models.hotel.services.AvailableServiceViewSimplified;
import com.horeca.site.models.hotel.translation.Translatable;

import java.util.List;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class HotelView {
	
	private Long id;

	@Translatable
	private String name;

	@Translatable
	private String description;

	@Translatable
	private String address;

	private String email;

	private String website;

	private String phone;

	private String bookingUrl;

	private String shopsUrl;

	private String restaurantsUrl;

	private String interestingPlacesUrl;

	private String eventsUrl;

	private String meteoUrl;

	private String fax;

	private Float starRating;

	private Integer rooms;

	private Float ratingOverall;

	@Translatable
	private String ratingOverallText;

	@Translatable
	private String propertyType;

	@Translatable
	private String chain;

	private Double longitude;

	private Double latitude;

	private Boolean isThrodiPartner;

	private Boolean isTestHotel;

	private Currency currency;

	@Translatable
	private List<String> tvChannels;

	@Translatable
	private UsefulInformation usefulInformation;

	@Translatable
	private RoomDirectory roomDirectory;

	@Translatable
	private List<AvailableServiceViewSimplified> services;

	private List<FileLink> images;

	@Translatable
	private List<Link> links;

	@JsonIgnore
	private Set<Guest> guests;

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

	public String getShopsUrl() {
		return shopsUrl;
	}

	public void setShopsUrl(String shopsUrl) {
		this.shopsUrl = shopsUrl;
	}

	public String getRestaurantsUrl() {
		return restaurantsUrl;
	}

	public void setRestaurantsUrl(String restaurantsUrl) {
		this.restaurantsUrl = restaurantsUrl;
	}

	public String getInterestingPlacesUrl() {
		return interestingPlacesUrl;
	}

	public void setInterestingPlacesUrl(String interestingPlacesUrl) {
		this.interestingPlacesUrl = interestingPlacesUrl;
	}

	public String getEventsUrl() {
		return eventsUrl;
	}

	public void setEventsUrl(String eventsUrl) {
		this.eventsUrl = eventsUrl;
	}

	public String getMeteoUrl() {
		return meteoUrl;
	}

	public void setMeteoUrl(String meteoUrl) {
		this.meteoUrl = meteoUrl;
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

	public Boolean getIsThrodiPartner() {
		return isThrodiPartner;
	}

	public void setIsThrodiPartner(Boolean isThrodiPartner) {
		this.isThrodiPartner = isThrodiPartner;
	}

	public Boolean getIsTestHotel() { return isTestHotel; }

	public void setIsTestHotel(Boolean isTestHotel) { this.isTestHotel = isTestHotel; }

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public List<String> getTvChannels() {
		return tvChannels;
	}

	public void setTvChannels(List<String> tvChannels) {
		this.tvChannels = tvChannels;
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

	public List<FileLink> getImages() {
		return images;
	}

	public void setImages(List<FileLink> images) {
		this.images = images;
	}

	public Set<Guest> getGuests() {
		return guests;
	}

	public void setGuests(Set<Guest> guests) {
		this.guests = guests;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}
}
