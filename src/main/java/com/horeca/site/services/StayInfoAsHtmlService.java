package com.horeca.site.services;

import com.horeca.site.models.guest.Guest;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.services.services.StayService;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobContainerPermissions;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.net.URISyntaxException;
import java.util.Optional;

@Service
public class StayInfoAsHtmlService {

    private static final String QR_CODES_DIRECTORY = "qr-codes";

    @Autowired
    private StayService stayService;
    @Autowired
    private QRCodeService qrCodeService;
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

    public String generate(String pin) {
        Stay stay = stayService.getWithoutCheckingStatus(pin);
        Guest guest = stay.getGuest();
        String guestName = guest.getFirstName() + " " + guest.getLastName();
        String roomNumber = stay.getRoomNumber();
        String arrival = stay.getFromDate().toString("dd-MM-yyyy");
        String departure = stay.getToDate().toString("dd-MM-yyyy");

        StringBuilder htmlBuilder = new StringBuilder();

        htmlBuilder.append( "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n");

        htmlBuilder.append(generateHead());

        htmlBuilder.append("<body>\n");
        htmlBuilder.append(generateBasicInfo(guestName, roomNumber, arrival, departure));
        htmlBuilder.append(generatePinPart(pin));
        htmlBuilder.append("</body>\n");

        htmlBuilder.append("</html>\n");

        return htmlBuilder.toString();
    }

    private String generateHead() {
        return "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Template</title>\n" +
                "    <style>\n" +
                "        @font-face {\n" +
                "            font-family: 'Lato', sans-serif;\n" +
                "            src: url('fonts/Lato-Regular.ttf');\n" +
                "            font-weight: normal;\n" +
                "            font-style: normal;\n" +
                "        }\n" +
                "\n" +
                "        @font-face {\n" +
                "            font-family: 'Lato', sans-serif;\n" +
                "            src: url('fonts/Lato-Bold.ttf');\n" +
                "            font-weight: bold;\n" +
                "            font-style: normal;\n" +
                "        }\n" +
                "\n" +
                "        @font-face {\n" +
                "            font-family: 'Lato', sans-serif;\n" +
                "            src: url('fonts/Lato-Italic.ttf');\n" +
                "            font-weight: normal;\n" +
                "            font-style: italic;\n" +
                "        }\n" +
                "\n" +
                "        body {\n" +
                "            margin: 20px;\n" +
                "            font-family: 'Lato', sans-serif;\n" +
                "        }\n" +
                "\n" +
                "        .header {\n" +
                "            font-size: large;\n" +
                "            font-weight: bold;\n" +
                "            text-transform: uppercase;\n" +
                "            margin-bottom: 15px;\n" +
                "        }\n" +
                "\n" +
                "        .entry {\n" +
                "            padding-bottom: 5px;\n" +
                "        }\n" +
                "\n" +
                "        .entry .key {\n" +
                "            display: inline-block;\n" +
                "            width: 130px;\n" +
                "            font-weight: bold;\n" +
                "        }\n" +
                "\n" +
                "        .entry .value {\n" +
                "            display: inline-block;\n" +
                "        }\n" +
                "\n" +
                "        .pin {\n" +
                "            margin-top: 30px;\n" +
                "        }\n" +
                "\n" +
                "        .pin .entry {\n" +
                "            margin-bottom: 10px;\n" +
                "        }\n" +
                "\n" +
                "        .pin .value {\n" +
                "            font-size: larger;\n" +
                "        }\n" +
                "\n" +
                "        .pin img {\n" +
                "            display: inline-block;\n" +
                "            width: 160px;\n" +
                "            height: 160px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>";
    }

    private String generateBasicInfo(String guestName, String roomNumber, String arrival, String departure) {
        return "<div class=\"header\">Information about the stay</div>\n" +
                "    <div class=\"entry\">\n" +
                "        <div class=\"key\">Guest's name:</div>\n" +
                "        <div class=\"value\">" + guestName + "</div>\n" +
                "    </div>\n" +
                "    <div class=\"entry\">\n" +
                "        <div class=\"key\">Room number:</div>\n" +
                "        <div class=\"value\">" + roomNumber + "</div>\n" +
                "    </div>\n" +
                "    <div class=\"entry\">\n" +
                "        <div class=\"key\">Arrival:</div>\n" +
                "        <div class=\"value\">" + arrival + "</div>\n" +
                "    </div>\n" +
                "    <div class=\"entry\">\n" +
                "        <div class=\"key\">Departure:</div>\n" +
                "        <div class=\"value\">" + departure + "</div>\n" +
                "    </div>";
    }

    private String generatePinPart(String pin) {
        String imageLink = getLinkForPin(pin);

        return "<div class=\"pin\">\n" +
                "        <div class=\"entry\">\n" +
                "            <div class=\"key\">PIN:</div>\n" +
                "            <div class=\"value\">" + pin + "</div>\n" +
                "        </div>\n" +
                "        <div class=\"entry\">\n" +
                "            <div class=\"key\" style=\"float: left;\">PIN QR Code:</div>\n" +
                "            <img src=\"" + imageLink + "\" alt=\"Mountain View\">\n" +
                "        </div>\n" +
                "    </div>";
    }

    private String getLinkForPin(String pin) {
        String uniqueFilename = QR_CODES_DIRECTORY + "/" + pin + ".jpg";
        Optional<String> imageLinkOpt = blobService.getLink(containerName, uniqueFilename);
        if (imageLinkOpt.isPresent()) {
            return imageLinkOpt.get();
        }
        else {
            byte[] imageBytes = qrCodeService.generate(pin);
            return blobService.upload(containerName, uniqueFilename, new ByteArrayInputStream(imageBytes));
        }
    }

    public void deleteAllCodeImages() {
        blobService.deleteAll(containerName, QR_CODES_DIRECTORY);
    }
}
