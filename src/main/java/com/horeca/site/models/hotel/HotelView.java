package com.horeca.site.models.hotel;

import com.horeca.site.models.hotel.address.AddressView;
import com.horeca.site.models.hotel.roomdirectory.RoomDirectoryView;
import com.horeca.site.models.hotel.services.DummyServices;

public class HotelView {
	
	private Long id;

	private String name;

	private AddressView address;
	
	private DummyServices services;

	private RoomDirectoryView roomDirectory;

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

	public AddressView getAddress() {
		return address;
	}

	public void setAddress(AddressView address) {
		this.address = address;
	}

	public DummyServices getServices() {
		return services;
	}

	public void setServices(DummyServices services) {
		this.services = services;
	}

	public RoomDirectoryView getRoomDirectory() {
		return roomDirectory;
	}

	public void setRoomDirectory(RoomDirectoryView roomDirectory) {
		this.roomDirectory = roomDirectory;
	}
}
