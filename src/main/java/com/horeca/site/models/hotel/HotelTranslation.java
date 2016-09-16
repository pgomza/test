package com.horeca.site.models.hotel;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.horeca.site.models.Translation;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class HotelTranslation implements Translation {

	@Id
	@GeneratedValue
    @JsonIgnore
	private Long id;

	@NotEmpty
	private String language;
	
	@NotEmpty
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
