package com.horeca.site.services;

import com.google.cloud.storage.*;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.hotel.images.FileLink;
import com.horeca.site.repositories.FileLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class HotelImagesService {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private FileLinkRepository fileLinkRepository;

    public FileLink get(Long hotelId, String filename) {
        FileLink foundLink = findByFilename(hotelId, filename);
        if (foundLink == null)
            throw new ResourceNotFoundException("An image with such a filename could not be found");

        return foundLink;
    }

    public Set<FileLink> getAll(Long hotelId) {
        return hotelService.get(hotelId).getImages();
    }

    public FileLink save(Long hotelId, String filename, InputStream imageStream) {
        String url = uploadToGAE(filename, imageStream);
        FileLink fileLink = new FileLink();
        fileLink.setFilename(filename);
        fileLink.setUrl(url);

        Hotel hotel = hotelService.get(hotelId);
        if (findByFilename(hotelId, filename) != null)
            delete(hotelId, filename);

        FileLink savedFileLink = fileLinkRepository.save(fileLink);
        hotel.getImages().add(savedFileLink);
        hotelService.update(hotelId, hotel);

        return savedFileLink;
    }

    public void delete(Long hotelId, String filename) {
        Hotel hotel = hotelService.get(hotelId);

        FileLink foundLink = findByFilename(hotelId, filename);
        if (foundLink == null)
            throw new ResourceNotFoundException("An image with such a filename could not be found");

        hotel.getImages().remove(foundLink);
        hotelService.update(hotelId, hotel);
    }

    private String uploadToGAE(String filename, InputStream imageStream) {
        List<Acl> acls = new ArrayList<>();
        acls.add(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));

        Storage storage = StorageOptions.getDefaultInstance().getService();
        Blob blob = storage.create(
                BlobInfo.newBuilder("horeca-club-backend", filename).setAcl(acls).build(), imageStream);

        return blob.getMediaLink();
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
