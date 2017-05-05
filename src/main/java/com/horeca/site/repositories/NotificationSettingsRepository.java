package com.horeca.site.repositories;

import com.horeca.site.models.notifications.NotificationSettings;
import org.springframework.data.repository.CrudRepository;

public interface NotificationSettingsRepository extends CrudRepository<NotificationSettings, Long> {
}
