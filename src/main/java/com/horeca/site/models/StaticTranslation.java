package com.horeca.site.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
public class StaticTranslation {

    @Id
    private String language;

    @NotNull
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "translation_id")
    private Set<StaticTranslationEntry> entries = new HashSet<>();

    StaticTranslation() {}

    public StaticTranslation(String language, Set<StaticTranslationEntry> entries) {
        this.language = language;
        this.entries = entries;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Set<StaticTranslationEntry> getEntries() {
        return entries;
    }

    public void setEntries(Set<StaticTranslationEntry> entries) {
        this.entries = entries;
    }
}
