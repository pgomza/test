package com.horeca.site.models;

import com.horeca.site.exceptions.BusinessRuleViolationException;

import javax.persistence.*;
import java.util.Set;

@MappedSuperclass
public abstract class Translatable<T extends Translation> {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn
    private Set<T> translations;

    public T getTranslation(String preferredLanguage, String defaultLanguage) {
        T defaultTranslation = null;
        T preferredTranslation = null;

        for (T translation : translations) {
            if (translation.getLanguage().equals(preferredLanguage)) {
                preferredTranslation = translation;
                break;
            }
            if (translation.getLanguage().equals(defaultLanguage))
                defaultTranslation = translation;
        }

        if (preferredTranslation == null) {
            if (defaultTranslation == null)
                throw new BusinessRuleViolationException("No default translation has been found");

            return defaultTranslation;
        }

        return preferredTranslation;
    }

    public Set<T> getTranslations() {
        return translations;
    }

    public void setTranslations(Set<T> translations) {
        this.translations = translations;
    }
}
