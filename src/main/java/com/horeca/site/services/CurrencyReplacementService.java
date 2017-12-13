package com.horeca.site.services;

import com.horeca.site.models.Currency;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@Service
@Transactional
public class CurrencyReplacementService {

    private static final Logger logger = Logger.getLogger(CurrencyReplacementService.class.getName());
    private static final Set<Class> classesNotIntrospectable;
    static {
        classesNotIntrospectable = new HashSet<>();
        classesNotIntrospectable.add(String.class);
        classesNotIntrospectable.add(Double.class);
        classesNotIntrospectable.add(Float.class);
        classesNotIntrospectable.add(Long.class);
        classesNotIntrospectable.add(Integer.class);
        classesNotIntrospectable.add(Short.class);
        classesNotIntrospectable.add(Character.class);
        classesNotIntrospectable.add(Byte.class);
        classesNotIntrospectable.add(Boolean.class);

        classesNotIntrospectable.add(LocalDate.class);
        classesNotIntrospectable.add(LocalDateTime.class);
        classesNotIntrospectable.add(LocalTime.class);
        classesNotIntrospectable.add(BigDecimal.class);
    }

    @Autowired
    private DeepCopyService deepCopyService;

    public <T> T replace(T object, Currency currency) {
        T objectCopy = deepCopyService.copy(object);
        try {
            doReplace(objectCopy, currency, new HashSet<>());
            return objectCopy;
        } catch (IllegalAccessException e) {
            logger.warning("Could not replace the currency in object " + object);
            // we need to make a fresh copy because 'entityCopy' may have some currencies already replaced
            return deepCopyService.copy(object);
        }
    }

    private void doReplace(Object object, Currency newCurrency, HashSet<Class<?>> alreadyIntrospected) throws IllegalAccessException {
        Class<?> objectClass = object.getClass();
        if (!alreadyIntrospected.contains(objectClass)) {
            alreadyIntrospected.add(objectClass);

            Field[] declaredFields = objectClass.getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                Class<?> fieldType = field.getType();

                if (Collection.class.isAssignableFrom(fieldType)) {
                    Collection<?> collection = (Collection<?>) field.get(object);
                    if (collection != null && !collection.isEmpty()) {
                        Class<?> elementType = CollectionTypeGuesser.guessElementType(collection);
                        if (shouldBeIntrospected(elementType)) {
                            for (Object subObject : collection) {
                                if (subObject != null) {
                                    doReplace(subObject, newCurrency, alreadyIntrospected);
                                }
                            }
                        }
                    }
                }
                else if (Currency.class.isAssignableFrom(fieldType)) {
                    field.set(object, newCurrency);
                }
                else if (shouldBeIntrospected(fieldType)) {
                    Object subObject = field.get(object);
                    if (subObject != null) {
                        doReplace(subObject, newCurrency, alreadyIntrospected);
                    }
                }
            }

            alreadyIntrospected.remove(objectClass);
        }
    }

    private static boolean shouldBeIntrospected(Class<?> type) {
        return !(type.isPrimitive() || classesNotIntrospectable.contains(type));
    }
}
