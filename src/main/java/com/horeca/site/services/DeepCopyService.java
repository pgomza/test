package com.horeca.site.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import java.io.IOException;

public class DeepCopyService {

    private static final Logger logger = Logger.getLogger(DeepCopyService.class);

    public static <T> T copy(T source) {
        ObjectMapper mapper = new ObjectMapper();
        Class<T> sourceClass = (Class<T>) source.getClass();
        try {
            String serialized = mapper.writeValueAsString(source);
            T copy = mapper.readValue(serialized, sourceClass);
            return copy;
        } catch (IOException e) {
            logger.warn("There was a problem while serializing an object of class " + sourceClass, e);
            return null;
        }
    }
}
