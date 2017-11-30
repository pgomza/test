package com.horeca.site.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class GenericPatchService {

    @Autowired
    private ObjectMapper objectMapper;

    public <T> void patch(T objectToPatch, Map<String, Object> updates) throws IOException, IllegalAccessException {
        Class<T> objectClass = (Class<T>) objectToPatch.getClass();
        Field[] declaredFields = objectClass.getDeclaredFields();
        Map<String, Field> nameToField = Arrays.stream(declaredFields)
                .collect(Collectors.toMap(Field::getName, Function.identity()));

        for (Map.Entry<String, Object> updateEntry : updates.entrySet()) {
            String updateKey = updateEntry.getKey();
            Object updateValue = updateEntry.getValue();

            Field fieldToUpdate = nameToField.get(updateKey);
            if (fieldToUpdate == null) {
                throw new RuntimeException(objectClass.getName() + " has no field named " + updateKey);
            }

            String updateValueAsString = objectMapper.writeValueAsString(updateValue);
            Object deserializedValue = objectMapper.readValue(updateValueAsString, fieldToUpdate.getType());

            fieldToUpdate.setAccessible(true);
            fieldToUpdate.set(objectToPatch, deserializedValue);
        }
    }
}
