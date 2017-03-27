package com.horeca.site.services;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.hotel.images.FileLink;
import com.horeca.site.repositories.FileLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class HotelImagesService {

    private Pattern filenamePattern = Pattern.compile("^\\w+(\\.\\w+)?$");

    @Value("${gae.bucketname}")
    private String bucketName;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private FileLinkRepository repository;

    public FileLink get(Long hotelId, String filename) {
        FileLink foundLink = findByFilename(hotelId, filename);
        if (foundLink == null)
            throw new ResourceNotFoundException("An image with such a filename could not be found");

        return foundLink;
    }

    public Set<FileLink> getAll(Long hotelId) {
        Hotel hotel = hotelService.get(hotelId);
        return hotel.getImages();
    }

    public FileLink save(Long hotelId, String filename, InputStream imageStream) {
        Matcher matcher = filenamePattern.matcher(filename);
        if (!matcher.matches()) {
            throw new BusinessRuleViolationException("Filename should only consist of alphanumeric characters. " +
                    "A file extension can be specified too. A sample valid filename: hotel.jpg");
        }

        // before uploading the file to GAE make it unique
        String uniqueFilename = "hotels/" + hotelId + "/" + filename;
        String url = uploadToGAE(uniqueFilename, imageStream);
        FileLink fileLink = new FileLink();
        fileLink.setFilename(filename);
        fileLink.setUrl(url);

        Hotel hotel = hotelService.get(hotelId);
        if (findByFilename(hotelId, filename) != null)
            delete(hotelId, filename);


        FileLink savedFileLink = repository.save(fileLink);
        hotel.getImages().add(fileLink);
        hotelService.update(hotel.getId(), hotel);
        return savedFileLink;
    }

    public void delete(Long hotelId, String filename) {
    }

    private String uploadToGAE(String filename, InputStream imageStream) {
        return null;
    }

    private FileLink findByFilename(Long hotelId, String filename) {
        Hotel hotel = hotelService.get(hotelId);

        FileLink foundLink = null;
        for (FileLink fileLink : hotel.getImages()) {
            if (fileLink.getFilename().equals(filename)) {
                foundLink = fileLink;
                break;
            }
        }

        return foundLink;
    }
}
