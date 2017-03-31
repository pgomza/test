package com.horeca.site.services;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.hotel.images.FileLink;
import com.horeca.site.repositories.FileLinkRepository;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class HotelImagesService {

    private static final Pattern filenamePattern = Pattern.compile("^\\w+(\\.\\w+)?$");

    @Autowired
    private HotelService hotelService;
    @Autowired
    private FileLinkRepository repository;

    private CloudBlobContainer container;

    @Value("${storage.connectionString}")
    private String storageConnectionString;
    @Value("${storage.containerName}")
    private String containerName;

    @PostConstruct
    void initStorageContainer() throws URISyntaxException, InvalidKeyException, StorageException {
        CloudStorageAccount account = CloudStorageAccount.parse(storageConnectionString);
        CloudBlobClient serviceClient = account.createCloudBlobClient();
        container = serviceClient.getContainerReference(containerName);
        container.createIfNotExists();

        BlobContainerPermissions containerPermissions = new BlobContainerPermissions();
        containerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);
        container.uploadPermissions(containerPermissions);
    }

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
        String url = uploadToContainer(uniqueFilename, imageStream);
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
        FileLink foundLink = get(hotelId, filename);
        if (foundLink == null)
            throw new ResourceNotFoundException("An image with such a filename could not be found");

        String uniqueFilename = "hotels/" + hotelId + "/" + filename;
        deleteFromContainer(uniqueFilename);

        Hotel hotel = hotelService.get(hotelId);
        hotel.getImages().remove(foundLink);
        hotelService.update(hotel.getId(), hotel);
        repository.delete(foundLink);
    }

    private String uploadToContainer(String filename, InputStream imageStream) {
        try {
            CloudBlockBlob blob = container.getBlockBlobReference(filename);
            blob.upload(imageStream, -1);
            CloudBlob uploadedBlob = container.getBlobReferenceFromServer(filename);
            return uploadedBlob.getUri().toString();
        } catch (URISyntaxException | StorageException | IOException e) {
            throw new RuntimeException("There was an error while trying to upload this file to Azure Storage");
        }
    }

    private void deleteFromContainer(String filename) {
        try {
            CloudBlob blob = container.getBlobReferenceFromServer(filename);
            blob.delete();
        } catch (URISyntaxException | StorageException e) {
            throw new RuntimeException("There was an error while trying to delete this file from Azure Storage");
        }
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
