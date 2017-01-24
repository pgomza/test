package com.horeca.site.services;

import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class HotelImagesService {

    @Autowired
    private HotelService hotelService;

    public String uploadToGAE(String filename, InputStream imageStream) {
        List<Acl> acls = new ArrayList<>();
        acls.add(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));

        Storage storage = StorageOptions.getDefaultInstance().getService();
        Blob blob = storage.create(
                BlobInfo.newBuilder("horeca-club-backend", filename).setAcl(acls).build(), imageStream);

        return blob.getMediaLink();
    }
}
