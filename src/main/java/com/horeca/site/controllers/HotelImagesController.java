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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

@Api(value = "hotels")
@RestController
@RequestMapping("/api/hotels")
public class HotelImagesController {

    @Autowired
    private HotelImagesService service;

    @RequestMapping(value = "/{id}/images/{filename:.+}", method = RequestMethod.PUT)
    public FileLink upload(@PathVariable("id") Long id, @PathVariable("filename") String filename, HttpServletRequest req)
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
        throw new BusinessRuleViolationException("Image uploading not available locally");
    }


}
