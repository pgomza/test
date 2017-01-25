package com.horeca.site.controllers;

import com.google.appengine.api.utils.SystemProperty;
import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.models.hotel.images.FileLink;
import com.horeca.site.services.HotelImagesService;
import io.swagger.annotations.Api;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

@Api(value = "hotels")
@RestController
@RequestMapping("/api/hotels")
public class HotelImagesController {

    @Autowired
    private HotelImagesService service;

    @RequestMapping(value = "/{id}/images/{filename:.+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public FileLink get(@PathVariable("id") Long id, @PathVariable("filename") String filename) {
        if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
            return service.get(id, filename);
        }
        else
            throw new BusinessRuleViolationException("Getting images is not available on localhost");
    }

    @RequestMapping(value = "/{id}/images", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<FileLink> getAll(@PathVariable("id") Long id) {
        if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
            return service.getAll(id);
        }
        else
            throw new BusinessRuleViolationException("Getting images is not available on localhost");
    }

    @RequestMapping(value = "/{id}/images/{filename:.+}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public FileLink save(@PathVariable("id") Long id, @PathVariable("filename") String filename, HttpServletRequest req)
            throws IOException, FileUploadException {
        if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
            ServletFileUpload upload = new ServletFileUpload();
            FileItemIterator iter = upload.getItemIterator(req);
            try {
                FileItemStream imageItem = iter.next();
                InputStream imageStream = imageItem.openStream();

                return service.save(id, filename, imageStream);
            } catch (Exception ex) { // TODO narrow down to a specific type and improve the message
                throw new RuntimeException("There was an error while processing the request");
            }
        }
        else
            throw new BusinessRuleViolationException("Uploading images is not available on localhost");
    }

    @RequestMapping(value = "/{id}/images/{filename:.+}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable("id") Long id, @PathVariable("filename") String filename) {
        if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
            service.delete(id, filename);
        }
        else
            throw new BusinessRuleViolationException("Deleting images is not available on localhost");
    }
}
