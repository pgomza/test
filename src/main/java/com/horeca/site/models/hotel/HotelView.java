package com.horeca.site.models.hotel;

import com.horeca.site.models.hotel.address.Address;
import com.horeca.site.models.hotel.gallery.Gallery;
import com.horeca.site.models.hotel.information.UsefulInformation;
import com.horeca.site.models.hotel.roomdirectory.RoomDirectory;
import com.horeca.site.models.hotel.services.AvailableServiceViewSimplified;

import java.util.List;
import java.util.Set;

public class HotelView {
	
	private Long id;
	private String name;
	private Address address;
	private UsefulInformation usefulInformation;
	private List<AvailableServiceViewSimplified> services;
	private RoomDirectory roomDirectory;
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

	public List<AvailableServiceViewSimplified> getServices() {
		return services;
	}

	public void setServices(List<AvailableServiceViewSimplified> services) {
		this.services = services;
	}

	public RoomDirectory getRoomDirectory() {
		return roomDirectory;
	}

	public void setRoomDirectory(RoomDirectory roomDirectory) {
		this.roomDirectory = roomDirectory;
	}

	public Set<Gallery> getGalleries() {
		return galleries;
	}

	public void setGalleries(Set<Gallery> galleries) {
		this.galleries = galleries;
	}
}
