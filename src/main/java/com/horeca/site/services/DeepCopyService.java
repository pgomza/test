package com.horeca.site.services;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class DeepCopyService {

    @Autowired
    private ObjectMapper objectMapper;

    public <T> T copy(T source) {
        Class<?> sourceClass = source.getClass();

        JavaType javaType = null;
        if (Collection.class.isAssignableFrom(sourceClass)) {
            Collection<?> collection = (Collection<?>) source;
            Class<?> elementType = CollectionTypeGuesser.guessElementType(collection);

            if (List.class.isAssignableFrom(sourceClass)) {
                javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, elementType);
            }
            else if (Set.class.isAssignableFrom(sourceClass)) {
                javaType = objectMapper.getTypeFactory().constructCollectionType(Set.class, elementType);
            }
            else {
                throw new RuntimeException("Unsupported type of collection");
            }
        }
        try {
            String serialized = objectMapper.writeValueAsString(source);
            if (javaType != null) {
                return (T) objectMapper.readValue(serialized, javaType);
            }
            else {
                return  (T) objectMapper.readValue(serialized, sourceClass);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not copy object " + source, e);
        }
    }
}
