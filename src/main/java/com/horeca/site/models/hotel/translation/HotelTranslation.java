package com.horeca.site.models.hotel.translation;

import com.horeca.site.models.hotel.Hotel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "HotelTranslation", uniqueConstraints = @UniqueConstraint(columnNames = {"languageCode", "hotel_id"}))
public class HotelTranslation {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "languageCode")
    private LanguageCode languageCode;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "hotel_translation_id")
    private Set<TranslationEntry> entries = new HashSet<>();

    HotelTranslation() {
    }

    public HotelTranslation(LanguageCode languageCode, Hotel hotel, Set<TranslationEntry> entries) {
        this.languageCode = languageCode;
        this.hotel = hotel;
        this.entries = entries;
    }

    public Map<String, String> asEntryMap() {
        return entries.stream().collect(Collectors.toMap(TranslationEntry::getOriginal, TranslationEntry::getTranslated));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public LanguageCode getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(LanguageCode languageCode) {
        this.languageCode = languageCode;
    }

    public Set<TranslationEntry> getEntries() {
        return entries;
    }

    public void setEntries(Set<TranslationEntry> entries) {
        this.entries = entries;
    }
}
