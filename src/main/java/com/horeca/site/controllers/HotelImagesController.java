package com.horeca.site.controllers;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.models.hotel.images.FileLink;
import com.horeca.site.services.HotelImagesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Api(value = "hotels")
@RestController
@RequestMapping("/api/hotels")
public class HotelImagesController {

    @Autowired
    private HotelImagesService service;

    @Value("${images.maxWidth}")
    private Integer maxImageWidth;

    @Value("${images.maxHeight}")
    private Integer maxImageHeight;

    @RequestMapping(value = "/{id}/images/{filename:.+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public FileLink get(@PathVariable("id") Long id, @PathVariable("filename") String filename) {
        return service.get(id, filename);
    }

    @RequestMapping(value = "/{id}/images", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<FileLink> getAll(@PathVariable("id") Long id) {
        return service.getAll(id);
    }

    @RequestMapping(value = "/{id}/images/{filename:.+}", method = RequestMethod.PUT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiImplicitParams(@ApiImplicitParam(dataType = "file", name = "file", paramType = "body"))
    public FileLink save(@RequestParam("file") MultipartFile file,
                         @PathVariable("id") Long id,
                         @PathVariable("filename") String filename)
            throws IOException, FileUploadException {

        // TODO check if this needs to be instantiated each time the method is invoked
        Tika tika = new Tika();
        BufferedInputStream bufferedStream = new BufferedInputStream(file.getInputStream());
        String mediaType = tika.detect(bufferedStream);

        if (mediaType.length() >= 5 && mediaType.substring(0, 5).equalsIgnoreCase("image")) {
            String format = mediaType.split("/")[1];
            InputStream scaled = service.resize(bufferedStream, format, maxImageWidth, maxImageHeight);
            return service.save(id, filename, scaled);
        }
        else
            throw new BusinessRuleViolationException("You are allowed to upload images only");

    }

    @RequestMapping(value = "/{id}/images/{filename:.+}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable("id") Long id, @PathVariable("filename") String filename) {
        service.delete(id, filename);
    }
}
