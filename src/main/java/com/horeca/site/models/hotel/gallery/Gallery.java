package com.horeca.site.models.hotel.gallery;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(indexes = @Index(name = "hotel_gallery", columnList = "hotel_gallery"))
public class Gallery {

    public enum Type { MAIN, SPA }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Type type;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> links;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Set<String> getLinks() {
        return links;
    }

    public void setLinks(Set<String> links) {
        this.links = links;
    }
}
