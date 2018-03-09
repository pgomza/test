package com.horeca.site.services;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Service
@Transactional
public class DeepCopyService {

    @Autowired
    private ObjectMapper objectMapper;

    public <T> T copy(T source) {
        T realSource = getRealObjectFromProxy(source);
        Class<?> sourceClass = realSource.getClass();

        JavaType javaType = null;
        if (Collection.class.isAssignableFrom(sourceClass)) {
            Collection<?> collection = (Collection<?>) realSource;

            if (List.class.isAssignableFrom(sourceClass)) {
                if (collection.isEmpty()) {
                    return (T) new ArrayList<>();
                }
                else {
                    Class<?> elementType = CollectionTypeGuesser.guessElementType(collection);
                    javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, elementType);
                }
            }
            else if (Set.class.isAssignableFrom(sourceClass)) {
                if (collection.isEmpty()) {
                    return (T) new HashSet<>();
                }
                else {
                    Class<?> elementType = CollectionTypeGuesser.guessElementType(collection);
                    javaType = objectMapper.getTypeFactory().constructCollectionType(Set.class, elementType);
                }
            }
            else {
                throw new RuntimeException("Unsupported type of collection");
            }
        }
        try {
            String serialized = objectMapper.writeValueAsString(realSource);
            if (javaType != null) {
                return (T) objectMapper.readValue(serialized, javaType);
            }
            else {
                return  (T) objectMapper.readValue(serialized, sourceClass);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not copy object " + realSource, e);
        }
    }

    private static <T> T getRealObjectFromProxy(T proxy) {
        if (proxy instanceof HibernateProxy) {
            return  (T) ((HibernateProxy) proxy).getHibernateLazyInitializer().getImplementation();
        }
        return proxy;
    }
}
