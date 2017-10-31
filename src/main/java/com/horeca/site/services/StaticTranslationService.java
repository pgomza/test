package com.horeca.site.services;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.StaticTranslation;
import com.horeca.site.models.StaticTranslationEntry;
import com.horeca.site.repositories.StaticTranslationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class StaticTranslationService {

    @Autowired
    private StaticTranslationRepository repository;

    public Set<StaticTranslation> getAll() {
        Set<StaticTranslation> translations = new HashSet<>();
        repository.findAll().forEach(translations::add);
        return translations;
    }

    public StaticTranslation get(String language) {
        StaticTranslation translation = repository.findOne(language);
        if (translation == null) {
            throw new ResourceNotFoundException();
        }
        return translation;
    }

    public StaticTranslation update(StaticTranslation updated) {
        return repository.save(updated);
    }

    public void delete(String language) {
        StaticTranslation translation = get(language);
        repository.delete(translation);
    }
}
