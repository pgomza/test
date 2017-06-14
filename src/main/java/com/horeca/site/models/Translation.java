package com.horeca.site.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@MappedSuperclass
public abstract class Translation<T> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "translatable_id")
    private T translatable;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Language language;

    public abstract T translate();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public T getTranslatable() {
        return translatable;
    }

    public void setTranslatable(T translatable) {
        this.translatable = translatable;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }
}
