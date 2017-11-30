package com.horeca.site.services.patchers;

import com.horeca.site.services.CollectionTypeGuesser;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class GenericPatchService {

    public void patch(Object objectToPatch, Map<String, Object> updates) throws IOException, IllegalAccessException {
        Class<?> objectClass = objectToPatch.getClass();
        Field[] declaredFields = objectClass.getDeclaredFields();
        Map<String, Field> nameToField = Arrays.stream(declaredFields)
                .collect(Collectors.toMap(Field::getName, Function.identity()));

        for (Map.Entry<String, Object> updateEntry : updates.entrySet()) {
            String entryKey = updateEntry.getKey();
            Object entryValue = updateEntry.getValue();

            Field fieldToUpdate = nameToField.get(entryKey);
            if (fieldToUpdate == null) {
                throw new RuntimeException(objectClass.getName() + " has no field named " + entryKey);
            }

            fieldToUpdate.setAccessible(true);
            Class<?> fieldClass = fieldToUpdate.getType();

            if (isPrimitiveOrString(fieldClass)) {
                updateFieldValue(fieldToUpdate, objectToPatch, entryValue);
            }
            else if (Collection.class.isAssignableFrom(fieldClass)) {
                Collection<?> collection = (Collection<?>) fieldToUpdate.get(objectToPatch);
                Class<?> elementType = CollectionTypeGuesser.guessElementType(collection);
                if (isPrimitiveOrString(elementType)) {
                    updateFieldValue(fieldToUpdate, objectToPatch, entryValue);
                }
                else {
                    for (Object element : collection) {
                        patch(element, (Map<String, Object>) entryValue);
                    }
                }
            }
            else {
                Object fieldValue = fieldToUpdate.get(objectToPatch);
                patch(fieldValue, (Map<String, Object>) entryValue);
            }
        }
    }

    protected abstract void updateFieldValue(Field field, Object objectToPatch, Object entryValue) throws IOException,
            IllegalAccessException;

    private static boolean isPrimitiveOrString(Class<?> type) {
        return type == String.class || type.isPrimitive() || type == Double.class || type == Float.class ||
                type == Long.class || type == Integer.class || type == Short.class || type == Character.class ||
                type == Byte.class || type == Boolean.class;
    }
}
