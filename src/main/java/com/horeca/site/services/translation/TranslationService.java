package com.horeca.site.services.translation;

import com.horeca.site.models.hotel.translation.Translatable;
import com.horeca.site.services.CollectionTypeGuesser;
import com.horeca.site.services.DeepCopyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Service
public class TranslationService {

    @Autowired
    private DeepCopyService deepCopyService;

    public <T> ResponseEntity<T> translate(ResponseEntity<T> entity, Map<String, String> translations)
            throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {

        ResponseEntity<T> entityCopy = deepCopyService.copy(entity);
        T entityCopyBody = entityCopy.getBody();
        Class<T> bodyClass = (Class<T>) entityCopyBody.getClass();

        if (Collection.class.isAssignableFrom(bodyClass)) {
            Collection<?> collection = (Collection<?>) entityCopyBody;
            Collection<?> translated = translate(collection, translations);
            return new ResponseEntity<>((T) translated, entity.getHeaders(), entity.getStatusCode());
        }
        else if (!isPrimitiveOrPrimitiveWrapper(bodyClass) && !String.class.isAssignableFrom(bodyClass)) {
            introspect(entityCopyBody, new HashSet<>(), translations);
        }
        return entityCopy;
    }

    public <T> Page<T> translate(Page<T> page, Map<String, String> translations)
            throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {

        Page<T> pageCopy = deepCopyService.copy(page);
        List<T> body = pageCopy.getContent();
        List<T> translated = (List<T>) introspectCollection(body, new HashSet<>(), translations);

        return new PageImpl<>(translated);
    }

    public <T> Collection<T> translate(Collection<T> collection, Map<String, String> translations)
            throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        Collection<T> collectionCopy = deepCopyService.copy(collection);
        return introspectCollection(collectionCopy, new HashSet<>(), translations);
    }

    public <T> T translate(T object, Map<String, String> translations)
            throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {

        T objectCopy = deepCopyService.copy(object);
        Class<T> objectClass = (Class<T>) objectCopy.getClass();
        if (!isPrimitiveOrPrimitiveWrapper(objectClass) && !String.class.isAssignableFrom(objectClass)) {
            introspect(objectCopy, new HashSet<>(), translations);
        }

        return objectCopy;
    }

    public <T> Set<String> extractTranslatableProps(T object) throws IllegalAccessException {
        Class<T> objectClass = (Class<T>) object.getClass();
        Set<String> extractedProps = new HashSet<>();
        if (!isPrimitiveOrPrimitiveWrapper(objectClass) && !String.class.isAssignableFrom(objectClass)) {
            T copy = deepCopyService.copy(object);
            collectProps(copy, new HashSet<>(), extractedProps);
        }
        return extractedProps;
    }

    private static void introspect(Object object, HashSet<Class<?>> introspected, Map<String, String> translations)
            throws IllegalAccessException, NoSuchMethodException, InstantiationException, InvocationTargetException {

        Class<?> objectClass = object.getClass();

        if (!introspected.contains(objectClass)) {

            introspected.add(objectClass);

            Field[] fields = objectClass.getDeclaredFields();
            for (Field field : fields) {

                field.setAccessible(true);
                if (!field.isAnnotationPresent(Translatable.class)) {
                    continue;
                }

                Class<?> fieldType = field.getType();
                if (fieldType == String.class) {
                    String toTranslate = (String) field.get(object);
                    if (isStringEligible(toTranslate)) {
                        String translated = translateString(toTranslate.trim(), translations);
                        field.set(object, translated);
                    }
                }
                else if (Collection.class.isAssignableFrom(fieldType)) {
                    Collection<?> collection = (Collection<?>) field.get(object);
                    if (collection != null) {
                        Collection<?> translated = introspectCollection(collection, introspected, translations);
                        field.set(object, translated);
                    }
                }
                else if (!isPrimitiveOrPrimitiveWrapper(fieldType)) {
                    Object fieldValue = field.get(object);
                    if (fieldValue != null) {
                        introspect(fieldValue, introspected, translations);
                    }
                }
            }
        }

        introspected.remove(objectClass);
    }

    private static <T> Collection<T> introspectCollection(Collection<T> collection, HashSet<Class<?>> introspected,
                                                          Map<String, String> translations)
            throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {

        Class<? extends Collection> collectionClass = collection.getClass();
        Collection<T> translatedCopy;
        if (Set.class.isAssignableFrom(collectionClass)) {
            translatedCopy = new HashSet<>(collection.size());
        }
        else if (List.class.isAssignableFrom(collectionClass)) {
            translatedCopy = new ArrayList<>(collection.size());
        }
        else {
            throw new RuntimeException("Unsupported type of collection");
        }

        if (!collection.isEmpty()) {
            Class<?> elementClass = CollectionTypeGuesser.guessElementType(collection);
            if (elementClass == String.class) {
                for (Object element : collection) {
                    String toTranslate = (String) element;
                    if (isStringEligible(toTranslate)) {
                        String translated = translateString(toTranslate.trim(), translations);
                        translatedCopy.add((T) translated);
                    }
                }
            }
            else if (Collection.class.isAssignableFrom(elementClass)) {
                for (Object element : collection) {
                    Collection<?> subCollection = (Collection<?>) element;
                    if (subCollection != null) {
                        translatedCopy.add((T) introspectCollection(subCollection, introspected, translations));
                    }
                }
            }
            else if (!isPrimitiveOrPrimitiveWrapper(elementClass)) {
                for (Object element : collection) {
                    if (element != null) {
                        introspect(element, introspected, translations);
                    }
                }
                return collection;
            }
        }
        return translatedCopy;
    }

    private static String translateString(String toTranslate, Map<String, String> translations) {
        String translation = translations.get(toTranslate);
        if (translation == null || translation.trim().isEmpty()) {
            return toTranslate;
        }
        else {
            return translation;
        }
    }

    private static void collectProps(Object object, HashSet<Class<?>> introspected, Set<String> results)
            throws IllegalAccessException {

        Class<?> objectClass = object.getClass();

        if (!introspected.contains(objectClass)) {

            introspected.add(objectClass);

            Field[] fields = objectClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (!field.isAnnotationPresent(Translatable.class)) {
                    continue;
                }

                Class<?> fieldType = field.getType();
                if (fieldType == String.class) {
                    String text = (String) field.get(object);
                    if (isStringEligible(text)) {
                        results.add(text.trim());
                    }
                }
                else if (Collection.class.isAssignableFrom(fieldType)) {
                    Collection<?> collection = (Collection<?>) field.get(object);
                    if (collection != null) {
                        collectPropsFromCollection(collection, introspected, results);
                    }
                }
                else if (!isPrimitiveOrPrimitiveWrapper(fieldType)) {
                    Object fieldValue = field.get(object);
                    if (fieldValue != null) {
                        collectProps(fieldValue, introspected, results);
                    }
                }
            }
        }

        introspected.remove(objectClass);
    }

    private static void collectPropsFromCollection(Collection<?> collection, HashSet<Class<?>> introspected,
                                                   Set<String> results) throws IllegalAccessException {

        if (!(collection instanceof List) && !(collection instanceof Set)){
            throw new RuntimeException("Unsupported type of collection");
        }

        if (!collection.isEmpty()) {
            Class<?> elementClass = CollectionTypeGuesser.guessElementType(collection);
            if (elementClass == String.class) {
                for (Object element : collection) {
                    String text = (String) element;
                    if (isStringEligible(text)) {
                        results.add(text.trim());
                    }
                }
            }
            else if (Collection.class.isAssignableFrom(elementClass)) {
                for (Object element : collection) {
                    Collection<?> subCollection = (Collection<?>) element;
                    if (subCollection != null) {
                        collectPropsFromCollection(subCollection, introspected, results);
                    }
                }
            }
            else if (!isPrimitiveOrPrimitiveWrapper(elementClass)) {
                for (Object element : collection) {
                    if (element != null) {
                        collectProps(element, introspected, results);
                    }
                }
            }
        }
    }

    private static boolean isPrimitiveOrPrimitiveWrapper(Class<?> type) {
        return type.isPrimitive() || type == Double.class || type == Float.class || type == Long.class ||
                type == Integer.class || type == Short.class || type == Character.class || type == Byte.class ||
                type == Boolean.class;
    }

    private static boolean isStringEligible(String text) {
        return text != null && !text.trim().isEmpty();
    }
}
