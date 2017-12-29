package com.horeca.site.services;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobContainerPermissions;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URISyntaxException;
import java.util.Optional;

@Service
public class QRCodeService {

    private static final String QR_CODES_DIRECTORY = "qr-codes";

    @Autowired
    private BlobService blobService;

    @Value("${storage.images.containerName}")
    private String containerName;

    @PostConstruct
    void registerImagesContainer() throws URISyntaxException, StorageException {
        BlobContainerPermissions containerPermissions = new BlobContainerPermissions();
        containerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);
        blobService.registerContainer(containerName, containerPermissions);
    }

    private byte[] generate(String text) {
        ByteArrayOutputStream qrStream = QRCode.from(text).withSize(160, 160).to(ImageType.JPG).stream();
        return qrStream.toByteArray();
    }

    String getLinkForPin(String pin) {
        String uniqueFilename = QR_CODES_DIRECTORY + "/" + pin + ".jpg";
        Optional<String> imageLinkOpt = blobService.getLink(containerName, uniqueFilename);
        if (imageLinkOpt.isPresent()) {
            return imageLinkOpt.get();
        }
        else {
            byte[] imageBytes = generate(pin);
            return blobService.upload(containerName, uniqueFilename, new ByteArrayInputStream(imageBytes));
        }
    }

    public void deleteAllCodeImages() {
        blobService.deleteAll(containerName, QR_CODES_DIRECTORY);
    }
}
