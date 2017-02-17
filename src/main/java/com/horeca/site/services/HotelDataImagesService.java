package com.horeca.site.services;

import com.google.cloud.storage.*;
import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.hotel.images.FileLink;
import com.horeca.site.models.hoteldata.HotelData;
import com.horeca.site.repositories.FileLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class HotelDataImagesService {

    private Pattern filenamePattern = Pattern.compile("^\\w+(\\.\\w+)?$");

    @Value("${gae.bucketname}")
    private String bucketName;

    @Autowired
    private HotelDataService hotelDataService;

    @Autowired
    private FileLinkRepository repository;

    public FileLink get(Long hotelDataId, String filename) {
        FileLink foundLink = findByFilename(hotelDataId, filename);
        if (foundLink == null)
            throw new ResourceNotFoundException("An image with such a filename could not be found");

        return foundLink;
    }

    public List<FileLink> getAll(Long hotelDataId) {
        HotelData hotelData = hotelDataService.get(hotelDataId);
        return hotelData.getImages();
    }

    public FileLink save(Long hotelDataId, String filename, InputStream imageStream) {
        Matcher matcher = filenamePattern.matcher(filename);
        if (!matcher.matches()) {
            throw new BusinessRuleViolationException("Filename should only consist of alphanumeric characters. " +
                    "A file extension can be specified too. A sample valid filename: hotel.jpg");
        }

        // before uploading the file to GAE make it unique
        String uniqueFilename = "hoteldata/" + hotelDataId + "/" + filename;
        String url = uploadToGAE(uniqueFilename, imageStream);
        FileLink fileLink = new FileLink();
        fileLink.setFilename(filename);
        fileLink.setUrl(url);

        HotelData hotelData = hotelDataService.get(hotelDataId);
        if (findByFilename(hotelDataId, filename) != null)
            delete(hotelDataId, filename);


        FileLink savedFileLink = repository.save(fileLink);
        hotelData.getImages().add(fileLink);
        hotelDataService.update(hotelData);
        return savedFileLink;
    }

    public void delete(Long hotelDataId, String filename) {
        FileLink foundLink = get(hotelDataId, filename);
        if (foundLink == null)
            throw new ResourceNotFoundException("An image with such a filename could not be found");

        Storage storage = StorageOptions.getDefaultInstance().getService();
        // map 'filename' to a unique filename on GAE
        String uniqueFilename = "hoteldata/" + hotelDataId + "/" + filename;
        BlobId blobId = BlobId.of(bucketName, uniqueFilename);

        try {
            boolean deleted = storage.delete(blobId);
            if (!deleted)
                throw new RuntimeException("There was an error while trying to delete this file");
        }
        catch (StorageException ex) {
            throw new RuntimeException("There was an error while trying to delete this file");
        }

        HotelData hotelData = hotelDataService.get(hotelDataId);
        hotelData.getImages().remove(foundLink);
        hotelDataService.update(hotelData);
    }

    private String uploadToGAE(String filename, InputStream imageStream) {
        List<Acl> acls = new ArrayList<>();
        acls.add(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));

        Storage storage = StorageOptions.getDefaultInstance().getService();
        Blob blob;

        try {
            blob = storage.create(BlobInfo.newBuilder(bucketName, filename).setAcl(acls).build(), imageStream);
        }
        catch (StorageException ex) {
            throw new RuntimeException("There was an error while trying to upload this file to GAE");
        }

        return blob.getMediaLink();
    }

    private FileLink findByFilename(Long hotelDataId, String filename) {
        HotelData hotelData = hotelDataService.get(hotelDataId);

        FileLink foundLink = null;
        for (FileLink fileLink : hotelData.getImages()) {
            if (fileLink.getFilename().equals(filename)) {
                foundLink = fileLink;
                break;
            }
        }

        return foundLink;
    }
}
