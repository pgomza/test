package com.horeca.site.repositories;

import com.horeca.site.models.TimeoutSettings;
import org.springframework.data.repository.CrudRepository;

public interface TimeoutSettingsRepository extends CrudRepository<TimeoutSettings, Long> {
}
