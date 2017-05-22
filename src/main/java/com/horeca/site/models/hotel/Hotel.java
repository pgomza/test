package com.horeca.site.models.hotel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.horeca.site.models.Currency;
import com.horeca.site.models.Price;
import com.horeca.site.models.guest.Guest;
import com.horeca.site.models.hotel.images.FileLink;
import com.horeca.site.models.hotel.information.UsefulInformation;
import com.horeca.site.models.hotel.roomdirectory.RoomDirectory;
import com.horeca.site.models.hotel.services.AvailableServiceType;
import com.horeca.site.models.hotel.services.AvailableServiceViewSimplified;
import com.horeca.site.models.hotel.services.AvailableServices;
import com.horeca.site.models.notifications.NotificationSettings;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Hotel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty
	private String name;

	@Column(columnDefinition = "nvarchar(4000)")
	public String description;

	@NotEmpty
	@Column(columnDefinition = "nvarchar(4000)")
	private String address;

	private String email;

	private String website;

	private String phone;

	@URL
	private String bookingUrl;

	@URL
	private String shopsUrl;

	@URL
	private String restaurantsUrl;

	@URL
	private String interestingPlacesUrl;

	@URL
	private String eventsUrl;

	private String fax;

	private Float starRating;

	private Integer rooms;

	private Float ratingOverall;

	private String ratingOverallText;

	private String propertyType;

	private String chain;

	private Double longitude;

	private Double latitude;

	private Boolean isThrodiPartner;

	@NotNull
	private Boolean isTestHotel;

	@NotNull
	@Enumerated(EnumType.STRING)
	private Currency currency;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn
	private UsefulInformation usefulInformation;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn
	private RoomDirectory roomDirectory;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn
	private AvailableServices availableServices;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "hotel_id")
	@OrderColumn(name = "filelink_order")
	private List<FileLink> images;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "Hotel_id")
	private Set<Guest> guests;

	@JsonIgnore
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "notificationSettings_id")
	private NotificationSettings notificationSettings;

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

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLatitude() {
		return latitude;
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

	public AvailableServices getAvailableServices() {
		return availableServices;
	}

	public void setAvailableServices(AvailableServices availableServices) {
		this.availableServices = availableServices;
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

	public NotificationSettings getNotificationSettings() {
		return notificationSettings;
	}

	public void setNotificationSettings(NotificationSettings notificationSettings) {
		this.notificationSettings = notificationSettings;
	}

	public HotelView toView() {
		HotelView hotelView = new HotelView();
		hotelView.setId(getId());
		hotelView.setName(getName());
		hotelView.setDescription(getDescription());
		hotelView.setAddress(getAddress());
		hotelView.setEmail(getEmail());
		hotelView.setWebsite(getWebsite());
		hotelView.setPhone(getPhone());
		hotelView.setBookingUrl(getBookingUrl());
		hotelView.setShopsUrl(getShopsUrl());
		hotelView.setRestaurantsUrl(getRestaurantsUrl());
		hotelView.setInterestingPlacesUrl(getInterestingPlacesUrl());
		hotelView.setEventsUrl(getEventsUrl());
		hotelView.setFax(getFax());
		hotelView.setStarRating(getStarRating());
		hotelView.setRooms(getRooms());
		hotelView.setRatingOverall(getRatingOverall());
		hotelView.setRatingOverallText(getRatingOverallText());
		hotelView.setPropertyType(getPropertyType());
		hotelView.setChain(getChain());
		hotelView.setLongitude(getLongitude());
		hotelView.setLatitude(getLatitude());
		hotelView.setIsThrodiPartner(getIsThrodiPartner());
		hotelView.setIsTestHotel(getIsTestHotel());
		hotelView.setCurrency(getCurrency());
		hotelView.setUsefulInformation(getUsefulInformation());
		hotelView.setRoomDirectory(getRoomDirectory());
		hotelView.setGuests(getGuests());

		AvailableServices availableServices = getAvailableServices();

		if (availableServices != null) {
			List<AvailableServiceViewSimplified> simplifiedList = new ArrayList<>();
			if (availableServices.getBreakfast() != null) {
				AvailableServiceViewSimplified simplified = new AvailableServiceViewSimplified();
				simplified.setType(AvailableServiceType.BREAKFAST);
				simplified.setPrice(availableServices.getBreakfast().getPrice());
				simplifiedList.add(simplified);
			}

			if (availableServices.getCarPark() != null) {
				AvailableServiceViewSimplified simplified = new AvailableServiceViewSimplified();
				simplified.setType(AvailableServiceType.CARPARK);
				simplified.setPrice(availableServices.getCarPark().getPrice());
				simplifiedList.add(simplified);
			}

			if (availableServices.getPetCare() != null) {
				AvailableServiceViewSimplified simplified = new AvailableServiceViewSimplified();
				simplified.setType(AvailableServiceType.PETCARE);
				simplified.setPrice(availableServices.getSpa().getPrice());
				simplifiedList.add(simplified);
			}

			if (availableServices.getTaxi() != null) {
				AvailableServiceViewSimplified simplified = new AvailableServiceViewSimplified();
				simplified.setType(AvailableServiceType.TAXI);
				simplified.setPrice(availableServices.getTaxi().getPrice());
				simplifiedList.add(simplified);
			}

			if (availableServices.getHousekeeping() != null) {
				AvailableServiceViewSimplified simplified = new AvailableServiceViewSimplified();
				simplified.setType(AvailableServiceType.HOUSEKEEPING);
				simplified.setPrice(availableServices.getHousekeeping().getPrice());
				simplifiedList.add(simplified);
			}

			if (availableServices.getRoomService() != null) {
				AvailableServiceViewSimplified simplified = new AvailableServiceViewSimplified();
				simplified.setType(AvailableServiceType.ROOMSERVICE);
				simplified.setPrice(availableServices.getRoomService().getPrice());
				simplifiedList.add(simplified);
			}

			if (availableServices.getTableOrdering() != null) {
				AvailableServiceViewSimplified simplified = new AvailableServiceViewSimplified();
				simplified.setType(AvailableServiceType.TABLEORDERING);
				Price price = new Price();
				price.setText("Free");
				simplified.setPrice(price);
				simplifiedList.add(simplified);
			}

			if (availableServices.getBar() != null) {
				AvailableServiceViewSimplified simplified = new AvailableServiceViewSimplified();
				simplified.setType(AvailableServiceType.BAR);
				simplified.setPrice(availableServices.getBar().getPrice());
				simplifiedList.add(simplified);
			}

			hotelView.setServices(simplifiedList);
		}

		hotelView.setImages(getImages());

		return hotelView;
	}
}
