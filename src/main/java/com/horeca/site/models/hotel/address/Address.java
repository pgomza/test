package com.horeca.site.models.hotel.address;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.horeca.site.models.Translatable;
import com.horeca.site.models.Viewable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Address extends Translatable<AddressTranslation> implements Viewable<AddressView> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public AddressView toView(String preferredLanguage, String defaultLanguage) {
		AddressTranslation translation = getTranslation(preferredLanguage, defaultLanguage);

		AddressView view = new AddressView();
		view.setCity(translation.getCity());
		view.setStreet(translation.getStreet());

		return view;
	}
}