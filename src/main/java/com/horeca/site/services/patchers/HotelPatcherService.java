package com.horeca.site.services.patchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.horeca.site.security.models.PermissionToPatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class HotelPatcherService extends GenericPatchService {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void updateFieldValue(Field field, Object objectToPatch, Object entryValue) throws IOException,
            IllegalAccessException {
        field.setAccessible(true);
        Class<?> fieldClass = field.getType();
        Object update = deserializeFromEntryValue(entryValue, fieldClass);

        PermissionToPatch permission = field.getAnnotation(PermissionToPatch.class);
        if (permission != null) {
            Set<String> currentPermissions = getRolesOfCurrentUser();
            String requiredPermission = permission.value();
            if (!currentPermissions.contains(requiredPermission)) {
                throw new RuntimeException("You lack permission to update field " + field.getName());
            }
        }
        field.set(objectToPatch, update);
    }

    private Object deserializeFromEntryValue(Object entry, Class<?> clazz) throws IOException {
        String updateValueAsString = objectMapper.writeValueAsString(entry);
        return objectMapper.readValue(updateValueAsString, clazz);
    }

    private Set<String> getRolesOfCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }
}
