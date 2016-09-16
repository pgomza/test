package com.horeca.site.models.hotel.address;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.horeca.site.models.Translatable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Address extends Translatable<AddressTranslation> {

	@Id
	@GeneratedValue
	@JsonIgnore
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}