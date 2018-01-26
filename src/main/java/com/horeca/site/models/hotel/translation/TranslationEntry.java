package com.horeca.site.models.hotel.translation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class TranslationEntry implements Comparable<TranslationEntry> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @NotEmpty
    private String original;

    @NotNull
    private String translated;

    TranslationEntry() {
    }

    public TranslationEntry(String original, String translated) {
        this.original = original;
        this.translated = translated;
    }

    @Override
    public int compareTo(TranslationEntry other) {
        return this.getOriginal().compareTo(other.getOriginal());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getTranslated() {
        return translated;
    }

    public void setTranslated(String translated) {
        this.translated = translated;
    }
}
