package com.horeca.site.models.hotel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.horeca.site.models.hotel.address.Address;
import com.horeca.site.models.hotel.gallery.Gallery;
import com.horeca.site.models.hotel.information.UsefulInformation;
import com.horeca.site.models.hotel.roomdirectory.RoomDirectory;
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
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long id;

	@NotEmpty
	private String name;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn
	private Address address;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn
	private UsefulInformation usefulInformation;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn
	private RoomDirectory roomDirectory;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn
	private AvailableServices availableServices;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn
	private Set<Gallery> galleries;

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

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
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

	public Set<Gallery> getGalleries() {
		return galleries;
	}

	public void setGalleries(Set<Gallery> galleries) {
		this.galleries = galleries;
	}

	public HotelView toView() {
		HotelView hotelView = new HotelView();
		hotelView.setId(getId());
		hotelView.setName(getName());
		hotelView.setAddress(getAddress());
		hotelView.setUsefulInformation(getUsefulInformation());
		hotelView.setRoomDirectory(getRoomDirectory());

		AvailableServices availableServices = getAvailableServices();

		List<AvailableServiceViewSimplified> simplifiedList = new ArrayList<>();
		if (availableServices.getBreakfast() != null) {
			AvailableServiceViewSimplified simplified = new AvailableServiceViewSimplified();
			simplified.setType(AvailableServiceViewSimplified.Type.BREAKFAST);
			simplified.setPrice(availableServices.getBreakfast().getPrice());
			simplifiedList.add(simplified);
		}

		if (availableServices.getCarPark() != null) {
			AvailableServiceViewSimplified simplified = new AvailableServiceViewSimplified();
			simplified.setType(AvailableServiceViewSimplified.Type.CARPARK);
			simplified.setPrice(availableServices.getCarPark().getPrice());
			simplifiedList.add(simplified);
		}

		if (availableServices.getRoomService() != null) {
			AvailableServiceViewSimplified simplified = new AvailableServiceViewSimplified();
			simplified.setType(AvailableServiceViewSimplified.Type.ROOMSERVICE);
			simplified.setPrice(availableServices.getRoomService().getPrice());
			simplifiedList.add(simplified);
		}

		if (availableServices.getReceptionCall() != null) {
			AvailableServiceViewSimplified simplified = new AvailableServiceViewSimplified();
			simplified.setType(AvailableServiceViewSimplified.Type.RECEPTIONCALL);
			simplified.setPrice(availableServices.getReceptionCall().getPrice());
			simplified.setAdditionalInfo(availableServices.getReceptionCall().getPhoneNumber());
			simplifiedList.add(simplified);
		}

		if (availableServices.getSpa() != null) {
			AvailableServiceViewSimplified simplified = new AvailableServiceViewSimplified();
			simplified.setType(AvailableServiceViewSimplified.Type.SPA);
			simplified.setPrice(availableServices.getSpa().getPrice());
			simplifiedList.add(simplified);
		}

		if (availableServices.getPetCare() != null) {
			AvailableServiceViewSimplified simplified = new AvailableServiceViewSimplified();
			simplified.setType(AvailableServiceViewSimplified.Type.PETCARE);
			simplified.setPrice(availableServices.getSpa().getPrice());
			simplifiedList.add(simplified);
		}

		if (availableServices.getTaxi() != null) {
			AvailableServiceViewSimplified simplified = new AvailableServiceViewSimplified();
			simplified.setType(AvailableServiceViewSimplified.Type.TAXI);
			simplified.setPrice(availableServices.getTaxi().getPrice());
			simplifiedList.add(simplified);
		}

		hotelView.setServices(simplifiedList);
		hotelView.setGalleries(getGalleries());

		return hotelView;
	}
}
