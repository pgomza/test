package com.horeca.site.models.hotel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.horeca.site.models.Translatable;
import com.horeca.site.models.Viewable;
import com.horeca.site.models.hotel.address.Address;
import com.horeca.site.models.hotel.gallery.Gallery;
import com.horeca.site.models.hotel.information.UsefulInformation;
import com.horeca.site.models.hotel.roomdirectory.RoomDirectory;
import com.horeca.site.models.hotel.services.AvailableServiceViewSimplified;
import com.horeca.site.models.hotel.services.AvailableServices;
import com.horeca.site.models.hotel.services.AvailableServicesView;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Hotel extends Translatable<HotelTranslation> implements Viewable<HotelView> {

	@Id
	@GeneratedValue
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long id;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn
	private Address address;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn
	private UsefulInformation usefulInformation;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn
	private RoomDirectory roomDirectory;

    @NotEmpty
	private String defaultTranslation;

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

	public String getDefaultTranslation() {
		return defaultTranslation;
	}

	public void setDefaultTranslation(String defaultTranslation) {
		this.defaultTranslation = defaultTranslation;
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

	@Override
	public HotelView toView(String preferredLanguage, String defaultLanguage) {
		HotelTranslation translation = getTranslation(preferredLanguage, defaultLanguage);

		HotelView hotelView = new HotelView();
		hotelView.setId(getId());
		hotelView.setName(translation.getName());
		hotelView.setAddress(getAddress().toView(preferredLanguage, defaultLanguage));
		hotelView.setUsefulInformation(getUsefulInformation());
		hotelView.setRoomDirectory(getRoomDirectory().toView(preferredLanguage, defaultLanguage));

		AvailableServicesView servicesView = getAvailableServices().toView(preferredLanguage, defaultLanguage);

		List<AvailableServiceViewSimplified> simplifiedList = new ArrayList<>();
		if (servicesView.getBreakfast() != null) {
			AvailableServiceViewSimplified simplified = new AvailableServiceViewSimplified();
			simplified.setType(AvailableServiceViewSimplified.Type.BREAKFAST);
			simplified.setPrice(servicesView.getBreakfast().getPrice());
			simplifiedList.add(simplified);
		}

		if (servicesView.getCarPark() != null) {
			AvailableServiceViewSimplified simplified = new AvailableServiceViewSimplified();
			simplified.setType(AvailableServiceViewSimplified.Type.CARPARK);
			simplified.setPrice(servicesView.getCarPark().getPrice());
			simplifiedList.add(simplified);
		}

		if (servicesView.getRoomService() != null) {
			AvailableServiceViewSimplified simplified = new AvailableServiceViewSimplified();
			simplified.setType(AvailableServiceViewSimplified.Type.ROOMSERVICE);
			simplified.setPrice(servicesView.getRoomService().getPrice());
			simplifiedList.add(simplified);
		}

		if (servicesView.getReceptionCall() != null) {
			AvailableServiceViewSimplified simplified = new AvailableServiceViewSimplified();
			simplified.setType(AvailableServiceViewSimplified.Type.RECEPTIONCALL);
			simplified.setPrice(servicesView.getReceptionCall().getPrice());
			simplified.setAdditionalInfo(servicesView.getReceptionCall().getPhoneNumber());
			simplifiedList.add(simplified);
		}

		if (servicesView.getSpa() != null) {
			AvailableServiceViewSimplified simplified = new AvailableServiceViewSimplified();
			simplified.setType(AvailableServiceViewSimplified.Type.SPA);
			simplified.setPrice(servicesView.getSpa().getPrice());
			simplifiedList.add(simplified);
		}

		if (servicesView.getPetCare() != null) {
			AvailableServiceViewSimplified simplified = new AvailableServiceViewSimplified();
			simplified.setType(AvailableServiceViewSimplified.Type.PETCARE);
			simplified.setPrice(servicesView.getSpa().getPrice());
			simplifiedList.add(simplified);
		}

		if (servicesView.getTaxi() != null) {
			AvailableServiceViewSimplified simplified = new AvailableServiceViewSimplified();
			simplified.setType(AvailableServiceViewSimplified.Type.TAXI);
			simplified.setPrice(servicesView.getTaxi().getPrice());
			simplifiedList.add(simplified);
		}

		hotelView.setServices(simplifiedList);

		hotelView.setGalleries(getGalleries());

		return hotelView;
	}
}
