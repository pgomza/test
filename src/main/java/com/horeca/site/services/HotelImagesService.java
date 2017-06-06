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
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class HotelImagesService {

    public static final String DEFAULT_FILENAME = "default.png";
    private static final Pattern filenamePattern = Pattern.compile("^[\\w\\-. ]+$");

    @Autowired
    private HotelService hotelService;
    @Autowired
    private FileLinkRepository repository;
    private CloudBlobContainer container;

    @Value("${storage.connectionString}")
    private String storageConnectionString;
    @Value("${storage.containerName}")
    private String containerName;
    @Value("${storage.maxImagesPerHotel}")
    private Integer maxImagesPerHotel;

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

    public List<FileLink> getAll(Long hotelId) {
        Hotel hotel = hotelService.get(hotelId);
        return hotel.getImages();
    }

    public FileLink save(Long hotelId, String filename, InputStream imageStream) {
        Hotel hotel = hotelService.get(hotelId);
        List<FileLink> hotelImages = hotel.getImages();

        if (hotelImages.size() >= maxImagesPerHotel) { // should never be '>', but just in case
            throw new BusinessRuleViolationException("A hotel can't have more than " + maxImagesPerHotel + " images " +
                    "associated with it");
        }

        Matcher matcher = filenamePattern.matcher(filename);
        if (!matcher.matches()) {
            throw new BusinessRuleViolationException("The filename should only consist of alphanumeric characters. " +
                    "A file extension can be specified too. A sample valid filename: hotel.jpg");
        }

        for (FileLink image : hotelImages) {
            if (image.getFilename().equals(filename)) {
                throw new BusinessRuleViolationException("An image with such a name already exists. Either delete the " +
                        "current one or choose a different name");
            }
        }

        // before uploading the file to GAE make it unique
        String uniqueFilename = "hotels/" + hotelId + "/" + filename;
        String url = uploadToContainer(uniqueFilename, imageStream);
        FileLink fileLink = new FileLink();
        fileLink.setFilename(filename);
        fileLink.setUrl(url);

        // TODO check this chunk of code - it can probably be removed
        if (findByFilename(hotelId, filename) != null)
            delete(hotelId, filename);

        FileLink savedFileLink = repository.save(fileLink);
        hotelImages.add(fileLink);
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

    public InputStream resize(InputStream imageStream, String format, int maxWidth, int maxHeight) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(imageStream);
        BufferedImage inputImage = ImageIO.read(bufferedInputStream);
        int imageWidth = inputImage.getWidth();
        int imageHeight = inputImage.getHeight();

        float widthRatio = maxWidth / (float) imageWidth;
        float heightRatio = maxHeight / (float) imageHeight;
        float minRatio = Math.min(widthRatio, heightRatio);

        if (minRatio < 1.0f) {
            int newImageWidth = Math.round(imageWidth * minRatio);
            int newImageHeight = Math.round(imageHeight * minRatio);

            BufferedImage outputImage = new BufferedImage(newImageWidth, newImageHeight, inputImage.getType());
            Graphics2D g2d = outputImage.createGraphics();
            g2d.drawImage(inputImage, 0, 0, newImageWidth, newImageHeight, null);
            g2d.dispose();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(outputImage, format, outputStream);

            return new BufferedInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        }
        else {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(inputImage, format, outputStream);
            return new BufferedInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        }
    }
}
