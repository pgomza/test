package com.horeca.site.models;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;

@Entity
@Table(name = "StaticTranslationEntry", uniqueConstraints = @UniqueConstraint(columnNames = {"translation_id", "original"}))
public class StaticTranslationEntry {

    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty
    private String original;

    @NotEmpty
    private String translated;

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
