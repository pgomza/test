package com.horeca.site.controllers;

import com.google.appengine.api.utils.SystemProperty;
import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.models.hotel.images.FileLink;
import com.horeca.site.services.HotelDataImagesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
import java.util.List;

@Api(value = "hotels")
@RestController
@RequestMapping("/api/hoteldata")
public class HotelDataImagesController {

    @Autowired
    private HotelDataImagesService service;

    @RequestMapping(value = "/{id}/images/{filename:.+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public FileLink get(@PathVariable("id") Long id, @PathVariable("filename") String filename) {
        return service.get(id, filename);
    }

    @RequestMapping(value = "/{id}/images", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<FileLink> getAll(@PathVariable("id") Long id) {
        return service.getAll(id);
    }

    @RequestMapping(value = "/{id}/images/{filename:.+}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiImplicitParams(@ApiImplicitParam(dataType = "file", name = "file", paramType = "body"))
    public FileLink save(@PathVariable("id") Long id, @PathVariable("filename") String filename, HttpServletRequest req)
            throws IOException, FileUploadException {
        if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
            ServletFileUpload upload = new ServletFileUpload();
            FileItemIterator iter = upload.getItemIterator(req);
            FileItemStream imageItem = iter.next();
            InputStream imageStream = imageItem.openStream();

            return service.save(id, filename, imageStream);
        }
        else
            throw new BusinessRuleViolationException("Uploading images is not available on localhost");
    }

    @RequestMapping(value = "/{id}/images/{filename:.+}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable("id") Long id, @PathVariable("filename") String filename) {
        service.delete(id, filename);
    }
}
