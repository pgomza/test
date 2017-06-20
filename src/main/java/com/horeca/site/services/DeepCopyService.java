package com.horeca.site.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DeepCopyService {

    private static final Logger logger = Logger.getLogger(DeepCopyService.class);

    @Autowired
    private MappingJackson2HttpMessageConverter converter;

    public synchronized <T> T deepCopy(T source) {
        ObjectMapper mapper = converter.getObjectMapper();
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
