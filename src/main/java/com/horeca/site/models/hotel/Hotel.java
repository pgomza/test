package com.horeca.site.models.hotel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.horeca.site.models.guest.Guest;
import com.horeca.site.models.hotel.images.FileLink;
import com.horeca.site.models.hotel.information.UsefulInformation;
import com.horeca.site.models.hotel.roomdirectory.RoomDirectory;
import com.horeca.site.models.hotel.services.AvailableServiceType;
import com.horeca.site.models.hotel.services.AvailableServiceViewSimplified;
import com.horeca.site.models.hotel.services.AvailableServices;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
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

	private Boolean isThrodiPartner;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn
	private UsefulInformation usefulInformation;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn
	private RoomDirectory roomDirectory;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn
	private AvailableServices availableServices;

	@ManyToMany
	@JoinTable(
			name = "Hotel_FileLink",
			joinColumns = @JoinColumn(name = "Hotel_id"),
			inverseJoinColumns = @JoinColumn(name = "FileLink_id")
	)
	private Set<FileLink> images;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "Hotel_id")
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

	public Set<FileLink> getImages() {
		return images;
	}

	public void setImages(Set<FileLink> images) {
		this.images = images;
	}

	public Set<Guest> getGuests() {
		return guests;
	}

	public void setGuests(Set<Guest> guests) {
		this.guests = guests;
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

			hotelView.setServices(simplifiedList);
		}

		hotelView.setImages(getImages());

		return hotelView;
	}
}
